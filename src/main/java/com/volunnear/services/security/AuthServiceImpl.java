package com.volunnear.services.security;

import com.volunnear.dtos.Credentials;
import com.volunnear.dtos.OrganisationDTO;
import com.volunnear.dtos.VolunteerDTO;
//import com.volunnear.dtos.jwt.JwtRequest; TODO
//import com.volunnear.dtos.jwt.JwtResponse; TODO
import com.volunnear.dtos.requests.RegistrationOrganisationRequestDTO;
import com.volunnear.dtos.requests.RegistrationVolunteerRequestDTO;
import com.volunnear.dtos.requests.UpdateOrganisationInfoRequestDTO;
import com.volunnear.dtos.requests.UpdateVolunteerInfoRequestDTO;
import com.volunnear.dtos.response.CurrentUserDTO;
import com.volunnear.dtos.response.OrganisationInfoDTO;
import com.volunnear.dtos.response.VolunteerInfoDTO;
import com.volunnear.entitiy.infos.OrganisationInfo;
import com.volunnear.entitiy.infos.VolunteerInfo;
import com.volunnear.entitiy.users.AppUser;
import com.volunnear.dtos.CustomUserDetails;
import com.volunnear.exceptions.auth.UserAlreadyExistsException;
import com.volunnear.exceptions.auth.UserNotFoundException;
//import com.volunnear.security.jwt.JwtTokenProvider;
import com.volunnear.services.interfaces.AuthService;
import com.volunnear.services.interfaces.OrganisationService;
import com.volunnear.services.interfaces.VolunteerService;
import com.volunnear.services.keycloak.KeycloakAdminClientService;
import com.volunnear.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final VolunteerService volunteerService;
//    private final JwtTokenProvider jwtTokenProvider;
    private final OrganisationService organisationService;
    private final KeycloakAdminClientService keycloakAdminClientService;

//    private final AuthenticationManager authenticationManager;

//    @Override
//    public JwtResponse createAuthToken(JwtRequest authRequest) {
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
//        CustomUserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
//        String token = jwtTokenProvider.createToken(userDetails);
//        return new JwtResponse(token);
//    }

    @Override
    public void registrationOfVolunteer(RegistrationVolunteerRequestDTO registrationVolunteerRequestDto) {
        String username = registrationVolunteerRequestDto.getUsername();
        userService.findAppUserByUsername(username).ifPresent(user -> {
            throw new UserAlreadyExistsException("User with username " + username + " already exist");
        });
        String password = registrationVolunteerRequestDto.getPassword();
        String realName = registrationVolunteerRequestDto.getRealName();
        String email = registrationVolunteerRequestDto.getEmail();
        volunteerService.registerVolunteer(new VolunteerDTO(realName, new Credentials(username, password, email)));
    }

    @Override
    public CurrentUserDTO getCurrentUser(Principal principal) {
        Optional<AppUser> appUserByUsername = userService.findAppUserByUsername(principal.getName());
        if (appUserByUsername.isPresent()) {
            AppUser user = appUserByUsername.get();
            return new CurrentUserDTO(user.getId(), user.getUsername(), user.getEmail());
        }
        return null;
    }

//    @Override
//    public void registrationOfOrganisation(RegistrationOrganisationRequestDTO registrationOrganisationRequestDTO) {
//        String username = registrationOrganisationRequestDTO.getUsername();
//        organisationService.findOrganisationByUsername(username).ifPresent(o -> {
//            throw new UserAlreadyExistsException("Organisation with username " + username + " already exists");
//        });
//
//        String password = registrationOrganisationRequestDTO.getPassword();
//        String email = registrationOrganisationRequestDTO.getEmail();
//        String nameOfOrganisation = registrationOrganisationRequestDTO.getNameOfOrganisation();
//        String country = registrationOrganisationRequestDTO.getCountry();
//        String city = registrationOrganisationRequestDTO.getCity();
//        String address = registrationOrganisationRequestDTO.getAddress();
//        organisationService.registerOrganisation(new OrganisationDTO(new Credentials(username, password, email), nameOfOrganisation, country, city, address));
//    }

    @Override
    @Transactional
    public void registrationOfOrganisation(RegistrationOrganisationRequestDTO registrationOrganisationRequestDTO) {
        String username = registrationOrganisationRequestDTO.getUsername();
        organisationService.findOrganisationByUsername(username).ifPresent(o -> {
            throw new UserAlreadyExistsException("Organisation with username " + username + " already exists");
        });

        String userId = null;
        try {
            // Создание пользователя в Keycloak
            userId = keycloakAdminClientService.createUser(registrationOrganisationRequestDTO);

            // Создание записи в базе данных
            OrganisationDTO organisationDTO = new OrganisationDTO(
                    new Credentials(username, registrationOrganisationRequestDTO.getPassword(), registrationOrganisationRequestDTO.getEmail()),
                    registrationOrganisationRequestDTO.getNameOfOrganisation(),
                    registrationOrganisationRequestDTO.getCountry(),
                    registrationOrganisationRequestDTO.getCity(),
                    registrationOrganisationRequestDTO.getAddress()
            );

            organisationService.registerOrganisation(organisationDTO);
        } catch (Exception e) {
            // Откат транзакции вручную, если была создана запись в Keycloak, но возникла ошибка при сохранении в БД
            if (userId != null) {
                keycloakAdminClientService.deleteUser(userId);
            }
            throw new RuntimeException("Registration failed", e);
        }
    }

    @Override
    public VolunteerInfoDTO updateVolunteerInfo(UpdateVolunteerInfoRequestDTO request, Principal principal) {
        AppUser user = userService.findAppUserByUsername(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("Volunteer not found"));

        VolunteerInfo volunteerInfo = volunteerService.getVolunteerInfo(user);
        volunteerInfo.setRealNameOfUser(request.getRealName());
        user.setEmail(request.getEmail());
        return volunteerService.updateVolunteerInfo(user, volunteerInfo);
    }

    @Override
    public OrganisationInfoDTO updateOrganisationInfo(UpdateOrganisationInfoRequestDTO request, Principal principal) {
        AppUser appUserByUsername = userService.findAppUserByUsername(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("Organisation not found"));

        OrganisationInfo infoAboutOrganisation = organisationService.findAdditionalInfoAboutOrganisation(appUserByUsername);
        infoAboutOrganisation.setNameOfOrganisation(request.getNameOfOrganisation());
        infoAboutOrganisation.setCountry(request.getCountry());
        infoAboutOrganisation.setCity(request.getCity());
        infoAboutOrganisation.setAddress(request.getAddress());
        appUserByUsername.setEmail(request.getEmail());
        return organisationService.updateOrganisationInfo(appUserByUsername, infoAboutOrganisation);
    }
}
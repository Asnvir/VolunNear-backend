package com.volunnear.services.security;

import com.volunnear.dtos.Credentials;
import com.volunnear.dtos.OrganisationDTO;
import com.volunnear.dtos.VolunteerDTO;
import com.volunnear.dtos.jwt.JwtRequest;
import com.volunnear.dtos.jwt.JwtResponse;
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
import com.volunnear.security.jwt.JwtTokenProvider;
import com.volunnear.services.interfaces.AuthService;
import com.volunnear.services.interfaces.OrganisationService;
import com.volunnear.services.interfaces.VolunteerService;
import com.volunnear.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import java.security.Principal;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final VolunteerService volunteerService;
    private final JwtTokenProvider jwtTokenProvider;
    private final OrganisationService organisationService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtResponse createAuthToken(JwtRequest authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        CustomUserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenProvider.createToken(userDetails);
        return new JwtResponse(token);
    }

    @Override
    public void registrationOfVolunteer(RegistrationVolunteerRequestDTO registrationVolunteerRequestDto) {
        String username = registrationVolunteerRequestDto.getUsername();
        userService.findAppUserByUsername(username);
        String password = registrationVolunteerRequestDto.getPassword();
        String realName = registrationVolunteerRequestDto.getRealName();
        String email = registrationVolunteerRequestDto.getEmail();
        volunteerService.registerVolunteer(new VolunteerDTO(realName, new Credentials(username, password, email)));
    }

    @Override
    public CurrentUserDTO getCurrentUser(Principal principal) {
        AppUser appUser =userService.findAppUserByUsername(principal.getName());
            return new CurrentUserDTO(appUser.getId(), appUser.getUsername(), appUser.getEmail());

    }

    @Override
    public void registrationOfOrganisation(RegistrationOrganisationRequestDTO registrationOrganisationRequestDTO) {
        String username = registrationOrganisationRequestDTO.getUsername();
        organisationService.findOrganisationByUsername(username);

        String password = registrationOrganisationRequestDTO.getPassword();
        String email = registrationOrganisationRequestDTO.getEmail();
        String nameOfOrganisation = registrationOrganisationRequestDTO.getNameOfOrganisation();
        String country = registrationOrganisationRequestDTO.getCountry();
        String city = registrationOrganisationRequestDTO.getCity();
        String address = registrationOrganisationRequestDTO.getAddress();
        organisationService.registerOrganisation(new OrganisationDTO(new Credentials(username, password, email), nameOfOrganisation, country, city, address));
    }

    @Override
    public VolunteerInfoDTO updateVolunteerInfo(UpdateVolunteerInfoRequestDTO request, Principal principal) {
        AppUser user = userService.findAppUserByUsername(principal.getName());
        VolunteerInfo volunteerInfo = volunteerService.getVolunteerInfo(user);
        volunteerInfo.setRealNameOfUser(request.getRealName());
        user.setEmail(request.getEmail());
        return volunteerService.updateVolunteerInfo(user, volunteerInfo);
    }

    @Override
    public OrganisationInfoDTO updateOrganisationInfo(UpdateOrganisationInfoRequestDTO request, Principal principal) {
        AppUser appUserByUsername = userService.findAppUserByUsername(principal.getName());
        OrganisationInfo infoAboutOrganisation = organisationService.findAdditionalInfoAboutOrganisation(appUserByUsername);
        infoAboutOrganisation.setNameOfOrganisation(request.getNameOfOrganisation());
        infoAboutOrganisation.setCountry(request.getCountry());
        infoAboutOrganisation.setCity(request.getCity());
        infoAboutOrganisation.setAddress(request.getAddress());
        appUserByUsername.setEmail(request.getEmail());
        return organisationService.updateOrganisationInfo(appUserByUsername, infoAboutOrganisation);
    }
}
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
import com.volunnear.entitiy.infos.OrganisationInfo;
import com.volunnear.entitiy.infos.VolunteerInfo;
import com.volunnear.entitiy.users.AppUser;
import com.volunnear.dtos.CustomUserDetails;
import com.volunnear.exceptions.AuthErrorException;
import com.volunnear.exceptions.RegistrationOfUserException;
import com.volunnear.security.jwt.JwtTokenProvider;
import com.volunnear.services.users.OrganisationService;
import com.volunnear.services.users.UserService;
import com.volunnear.services.users.VolunteerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final VolunteerService volunteerService;
    private final JwtTokenProvider jwtTokenProvider;
    private final OrganisationService organisationService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> createAuthToken(JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AuthErrorException(HttpStatus.UNAUTHORIZED.value(), "Incorrect login or password"), HttpStatus.UNAUTHORIZED);
        }
        CustomUserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenProvider.createToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<?> registrationOfVolunteer(RegistrationVolunteerRequestDTO registrationVolunteerRequestDto) {
        String username = registrationVolunteerRequestDto.getUsername();
        if (userService.findAppUserByUsername(username).isEmpty()) {
            String password = registrationVolunteerRequestDto.getPassword();
            String realName = registrationVolunteerRequestDto.getRealName();
            String email = registrationVolunteerRequestDto.getEmail();
            volunteerService.registerVolunteer(new VolunteerDTO(realName, new Credentials(username, password, email)));
            return new ResponseEntity<>(HttpStatus.OK);
        } else return new ResponseEntity<>(
                new RegistrationOfUserException(HttpStatus.BAD_REQUEST.value(), "User with username " + username + " already exist"),
                HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> registrationOfOrganisation(RegistrationOrganisationRequestDTO registrationOrganisationRequestDTO) {
        String username = registrationOrganisationRequestDTO.getUsername();
        if (organisationService.findOrganisationByUsername(username).isEmpty()) {
            String password = registrationOrganisationRequestDTO.getPassword();
            String email = registrationOrganisationRequestDTO.getEmail();
            String nameOfOrganisation = registrationOrganisationRequestDTO.getNameOfOrganisation();
            String country = registrationOrganisationRequestDTO.getCountry();
            String city = registrationOrganisationRequestDTO.getCity();
            String address = registrationOrganisationRequestDTO.getAddress();
            organisationService.registerOrganisation(new OrganisationDTO(new Credentials(username, password, email), nameOfOrganisation, country, city, address));
            return new ResponseEntity<>(HttpStatus.OK);
        } else return new ResponseEntity<>(
                new RegistrationOfUserException(HttpStatus.BAD_REQUEST.value(), "Organisation with username " + username + " already exists"),
                HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> updateVolunteerInfo(UpdateVolunteerInfoRequestDTO request, Principal principal) {
        Optional<AppUser> appUserByUsername = userService.findAppUserByUsername(principal.getName());
        if (appUserByUsername.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
        VolunteerInfo volunteerInfo = volunteerService.getVolunteerInfo(appUserByUsername.get());
        volunteerInfo.setRealNameOfUser(request.getRealName());
        appUserByUsername.get().setEmail(request.getEmail());
        volunteerService.updateVolunteerInfo(appUserByUsername.get(), volunteerInfo);
        return new ResponseEntity<>("Successfully updated user info", HttpStatus.OK);
    }

    public ResponseEntity<?> updateOrganisationInfo(UpdateOrganisationInfoRequestDTO request, Principal principal) {
        Optional<AppUser> appUserByUsername = userService.findAppUserByUsername(principal.getName());
        if (appUserByUsername.isEmpty()) {
            return new ResponseEntity<>("Organisation not found", HttpStatus.BAD_REQUEST);
        }
        OrganisationInfo infoAboutOrganisation = organisationService.findAdditionalInfoAboutOrganisation(appUserByUsername.get());

        infoAboutOrganisation.setNameOfOrganisation(request.getNameOfOrganisation());
        infoAboutOrganisation.setCountry(request.getCountry());
        infoAboutOrganisation.setCity(request.getCity());
        infoAboutOrganisation.setAddress(request.getAddress());
        appUserByUsername.get().setEmail(request.getEmail());

        organisationService.updateOrganisationInfo(appUserByUsername.get(), infoAboutOrganisation);
        return new ResponseEntity<>("Successfully updated organisation info", HttpStatus.OK);
    }
}
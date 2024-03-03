package com.volunnear.services.security;

import com.volunnear.dtos.Credentials;
import com.volunnear.dtos.OrganisationDTO;
import com.volunnear.dtos.VolunteerDTO;
import com.volunnear.dtos.jwt.JwtRequest;
import com.volunnear.dtos.jwt.JwtResponse;
import com.volunnear.dtos.requests.RegistrationOrganisationRequestDTO;
import com.volunnear.dtos.requests.RegistrationVolunteerRequestDTO;
import com.volunnear.exceptions.AuthErrorException;
import com.volunnear.exceptions.RegistrationOfUserException;
import com.volunnear.security.jwt.JwtTokenProvider;
import com.volunnear.services.OrganisationService;
import com.volunnear.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final OrganisationService organisationService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> createAuthToken(JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AuthErrorException(HttpStatus.UNAUTHORIZED.value(), "Incorrect login or password"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenProvider.createToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<?> registrationOfVolunteer(RegistrationVolunteerRequestDTO registrationVolunteerRequestDto) {
        String username = registrationVolunteerRequestDto.getUsername();
        if (userService.findByUsername(username).isEmpty()) {
            String password = registrationVolunteerRequestDto.getPassword();
            String realName = registrationVolunteerRequestDto.getRealName();
            userService.registerVolunteer(new VolunteerDTO(realName, new Credentials(username, password)));
            return new ResponseEntity<>(HttpStatus.OK);
        } else return new ResponseEntity<>(
                new RegistrationOfUserException(HttpStatus.BAD_REQUEST.value(), "User with username " + username + " already exist"),
                HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> registrationOfOrganisation(RegistrationOrganisationRequestDTO registrationOrganisationRequestDTO) {
        String username = registrationOrganisationRequestDTO.getUsername();
        if (organisationService.findOrganisationByUsername(username).isEmpty()) {
            String password = registrationOrganisationRequestDTO.getPassword();
            String nameOfOrganisation = registrationOrganisationRequestDTO.getNameOfOrganisation();
            String country = registrationOrganisationRequestDTO.getCountry();
            String city = registrationOrganisationRequestDTO.getCity();
            String address = registrationOrganisationRequestDTO.getAddress();
            String industry = registrationOrganisationRequestDTO.getIndustry();
            organisationService.registerOrganisation(new OrganisationDTO(new Credentials(username, password), nameOfOrganisation, country, city, address, industry));
            return new ResponseEntity<>(HttpStatus.OK);
        } else return  new ResponseEntity<>(
                new RegistrationOfUserException(HttpStatus.BAD_REQUEST.value(), "Organisation with username " + username + " already exists"),
                HttpStatus.BAD_REQUEST);
    }
}

package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.jwt.JwtRequest;
import com.volunnear.dtos.requests.RegistrationOrganisationRequestDTO;
import com.volunnear.dtos.requests.RegistrationVolunteerRequestDTO;
import com.volunnear.dtos.requests.UpdateOrganisationInfoRequestDTO;
import com.volunnear.dtos.requests.UpdateVolunteerInfoRequestDTO;
import com.volunnear.services.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping(value = Routes.LOGIN)
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @PostMapping(value = Routes.REGISTER_VOLUNTEER, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registrationOfVolunteer(@RequestBody RegistrationVolunteerRequestDTO registrationVolunteerRequestDto) {
        return authService.registrationOfVolunteer(registrationVolunteerRequestDto);
    }

    @PostMapping(value = Routes.REGISTER_ORGANISATION, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registrationOfOrganisation(@RequestBody RegistrationOrganisationRequestDTO registrationOrganisationRequestDTO) {
        return authService.registrationOfOrganisation(registrationOrganisationRequestDTO);
    }

    @PutMapping(value = Routes.UPDATE_VOLUNTEER_PROFILE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateVolunteerInfo(@RequestBody UpdateVolunteerInfoRequestDTO updateVolunteerInfoRequestDTO, Principal principal) {
        return authService.updateVolunteerInfo(updateVolunteerInfoRequestDTO, principal);
    }

    @PutMapping(value = Routes.UPDATE_ORGANISATION_PROFILE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateOrganisationInfo(@RequestBody UpdateOrganisationInfoRequestDTO updateOrganisationInfoRequest, Principal principal) {
        return authService.updateOrganisationInfo(updateOrganisationInfoRequest, principal);
    }
}

package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.jwt.JwtRequest;
import com.volunnear.dtos.requests.RegistrationOrganisationRequestDTO;
import com.volunnear.dtos.requests.RegistrationVolunteerRequestDTO;
import com.volunnear.dtos.requests.UpdateOrganisationInfoRequestDTO;
import com.volunnear.dtos.requests.UpdateVolunteerInfoRequestDTO;
import com.volunnear.dtos.response.CurrentUserDTO;
import com.volunnear.services.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping(value = Routes.LOGIN)
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @GetMapping(value = Routes.GET_CURRENT_USER)
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        CurrentUserDTO currentUserDTO = authService.getCurrentUser(principal);
        if (currentUserDTO == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(currentUserDTO, HttpStatus.OK);
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

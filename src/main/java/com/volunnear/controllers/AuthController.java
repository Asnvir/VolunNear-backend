package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.jwt.JwtRequest;
import com.volunnear.dtos.jwt.JwtResponse;
import com.volunnear.dtos.requests.*;
import com.volunnear.dtos.response.CurrentUserDTO;
import com.volunnear.dtos.response.OrganisationInfoDTO;
import com.volunnear.dtos.response.VolunteerInfoDTO;
import com.volunnear.services.interfaces.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    public ResponseEntity<JwtResponse> createAuthToken(@RequestBody JwtRequest authRequest) {
        JwtResponse jwtResponse = authService.createAuthToken(authRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(jwtResponse);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = Routes.GET_CURRENT_USER)
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        CurrentUserDTO currentUserDTO = authService.getCurrentUser(principal);
        if (currentUserDTO == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(currentUserDTO, HttpStatus.OK);
    }

    @PostMapping(value = Routes.REGISTER_VOLUNTEER, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> registrationOfVolunteer(@RequestBody RegistrationVolunteerRequestDTO registrationVolunteerRequestDto) {
        authService.registrationOfVolunteer(registrationVolunteerRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = Routes.REGISTER_ORGANISATION, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> registrationOfOrganisation(@RequestBody RegistrationOrganisationRequestDTO registrationOrganisationRequestDTO) {
        authService.registrationOfOrganisation(registrationOrganisationRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping(value = Routes.UPDATE_VOLUNTEER_PROFILE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VolunteerInfoDTO> updateVolunteerInfo(@RequestBody UpdateVolunteerInfoRequestDTO updateVolunteerInfoRequestDTO, Principal principal) {
        VolunteerInfoDTO volunteerInfoDTO = authService.updateVolunteerInfo(updateVolunteerInfoRequestDTO, principal);
        return new ResponseEntity<>(volunteerInfoDTO, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping(value = Routes.UPDATE_ORGANISATION_PROFILE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrganisationInfoDTO> updateOrganisationInfo(@RequestBody UpdateOrganisationInfoRequestDTO updateOrganisationInfoRequest, Principal principal) {
        OrganisationInfoDTO organisationInfoDTO = authService.updateOrganisationInfo(updateOrganisationInfoRequest, principal);
        return new ResponseEntity<>(organisationInfoDTO, HttpStatus.OK);
    }

//    @SecurityRequirement(name = "Bearer Authentication")
//    @PostMapping(value = Routes.CHANGE_PASSWORD)
//    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequestDTO changePasswordRequestDTO, Principal principal) {
//        authService.changePassword(changePasswordRequestDTO, principal);
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }
}

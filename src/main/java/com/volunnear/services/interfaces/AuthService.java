package com.volunnear.services.interfaces;

import com.volunnear.dtos.jwt.JwtRequest;
import com.volunnear.dtos.requests.RegistrationOrganisationRequestDTO;
import com.volunnear.dtos.requests.RegistrationVolunteerRequestDTO;
import com.volunnear.dtos.requests.UpdateOrganisationInfoRequestDTO;
import com.volunnear.dtos.requests.UpdateVolunteerInfoRequestDTO;
import com.volunnear.dtos.response.CurrentUserDTO;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface AuthService {

    ResponseEntity<?> createAuthToken(JwtRequest authRequest);

    ResponseEntity<?> registrationOfVolunteer(RegistrationVolunteerRequestDTO registrationVolunteerRequestDto);

    CurrentUserDTO getCurrentUser(Principal principal);

    ResponseEntity<?> registrationOfOrganisation(RegistrationOrganisationRequestDTO registrationOrganisationRequestDTO);

    ResponseEntity<?> updateVolunteerInfo(UpdateVolunteerInfoRequestDTO request, Principal principal);

    ResponseEntity<?> updateOrganisationInfo(UpdateOrganisationInfoRequestDTO request, Principal principal);
}

package com.volunnear.services.interfaces;
//import com.volunnear.dtos.jwt.JwtRequest; TODO
//import com.volunnear.dtos.jwt.JwtResponse; TODO
import com.volunnear.dtos.requests.RegistrationOrganisationRequestDTO;
import com.volunnear.dtos.requests.RegistrationVolunteerRequestDTO;
import com.volunnear.dtos.requests.UpdateOrganisationInfoRequestDTO;
import com.volunnear.dtos.requests.UpdateVolunteerInfoRequestDTO;
import com.volunnear.dtos.response.CurrentUserDTO;
import com.volunnear.dtos.response.OrganisationInfoDTO;
import com.volunnear.dtos.response.VolunteerInfoDTO;
import java.security.Principal;

public interface AuthService {

//    JwtResponse createAuthToken(JwtRequest authRequest);

    void registrationOfVolunteer(RegistrationVolunteerRequestDTO registrationVolunteerRequestDto);

    CurrentUserDTO getCurrentUser(Principal principal);

    void registrationOfOrganisation(RegistrationOrganisationRequestDTO registrationOrganisationRequestDTO);

    VolunteerInfoDTO updateVolunteerInfo(UpdateVolunteerInfoRequestDTO request, Principal principal);

    OrganisationInfoDTO updateOrganisationInfo(UpdateOrganisationInfoRequestDTO request, Principal principal);
}

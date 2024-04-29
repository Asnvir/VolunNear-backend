package com.volunnear.services.interfaces;

import com.volunnear.dtos.requests.FeedbackRequest;
import com.volunnear.dtos.response.FeedbackResponseDTO;
import com.volunnear.dtos.response.OrganisationResponseDTO;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface FeedbackService {

    ResponseEntity<String> postFeedbackAboutOrganisation(FeedbackRequest feedbackRequest, Principal principal);

    ResponseEntity<String> updateFeedbackInfoForCurrentOrganisation(UUID idOfFeedback, FeedbackRequest feedbackRequest, Principal principal);

    ResponseEntity<String> deleteFeedbackAboutOrganisation(UUID idOfFeedback, Principal principal);

    Map<OrganisationResponseDTO, List<FeedbackResponseDTO>> getAllFeedbacksAboutAllOrganisations();

    ResponseEntity<?> getFeedbacksAboutCurrentOrganisation(UUID id);


}

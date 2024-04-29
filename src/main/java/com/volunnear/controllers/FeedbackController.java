package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.requests.FeedbackRequest;
import com.volunnear.dtos.response.FeedbackResponseDTO;
import com.volunnear.dtos.response.OrganisationResponseDTO;
import com.volunnear.services.interfaces.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @GetMapping(Routes.GET_FEEDBACKS_OF_ALL_ORGANISATIONS)
    public Map<OrganisationResponseDTO, List<FeedbackResponseDTO>> getAllFeedbacksAboutAllOrganisations() {
        return feedbackService.getAllFeedbacksAboutAllOrganisations();
    }

    @Operation(summary = "Get feedbacks from selected organisation", description = "Requires id of organisation. Returns " +
            "Map<OrganisationResponseDTO, List<FeedbackResponseDTO>>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feedbacks of current organisation"),
            @ApiResponse(responseCode = "400", description = "There is no feedback about that organisation"),
    })
    @GetMapping(Routes.GET_FEEDBACKS_FROM_CURRENT_ORGANISATION)
    public ResponseEntity<?> getFeedbacksAboutCurrentOrganisation(@RequestParam UUID id) {
        return feedbackService.getFeedbacksAboutCurrentOrganisation(id);
    }

    @PostMapping(Routes.POST_FEEDBACK_ABOUT_ORGANISATION)
    public ResponseEntity<String> postFeedbackAboutOrganisation(@RequestBody FeedbackRequest feedbackRequest, Principal principal) {
        return feedbackService.postFeedbackAboutOrganisation(feedbackRequest, principal);
    }

    @PutMapping(Routes.UPDATE_FEEDBACK_FOR_CURRENT_ORGANISATION)
    public ResponseEntity<String> updateFeedbackInfoForCurrentOrganisation(@RequestParam UUID idOfFeedback,
                                                                           @RequestBody FeedbackRequest feedbackRequest, Principal principal) {
        return feedbackService.updateFeedbackInfoForCurrentOrganisation(idOfFeedback, feedbackRequest, principal);
    }

    @DeleteMapping(Routes.DELETE_FEEDBACK_ABOUT_ORGANISATION)
    public ResponseEntity<String> deleteFeedbackAboutOrganisation(@RequestParam UUID idOfFeedback, Principal principal) {
        return feedbackService.deleteFeedbackAboutOrganisation(idOfFeedback, principal);
    }
}

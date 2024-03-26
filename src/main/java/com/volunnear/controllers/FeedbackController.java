package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.requests.FeedbackRequest;
import com.volunnear.services.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @GetMapping(Routes.GET_FEEDBACKS_OF_ALL_ORGANISATIONS)
    public ResponseEntity<?> getAllFeedbacksAboutAllOrganisations() {
        return feedbackService.getAllFeedbacksAboutAllOrganisations();
    }

    @GetMapping(Routes.GET_FEEDBACKS_FROM_CURRENT_ORGANISATION)
    public ResponseEntity<?> getFeedbacksAboutCurrentOrganisation(@RequestParam Long id) {
        return feedbackService.getFeedbacksAboutCurrentOrganisation(id);
    }

    @PostMapping(Routes.POST_FEEDBACK_ABOUT_ORGANISATION)
    public ResponseEntity<?> postFeedbackAboutOrganisation(@RequestBody FeedbackRequest feedbackRequest, Principal principal) {
        return feedbackService.postFeedbackAboutOrganisation(feedbackRequest, principal);
    }

    @PutMapping(Routes.UPDATE_FEEDBACK_FOR_CURRENT_ORGANISATION)
    public ResponseEntity<?> updateFeedbackInfoForCurrentOrganisation(@RequestParam Long idOfFeedback,
                                                                      @RequestBody FeedbackRequest feedbackRequest, Principal principal) {
        return feedbackService.updateFeedbackInfoForCurrentOrganisation(idOfFeedback, feedbackRequest, principal);
    }

    @DeleteMapping(Routes.DELETE_FEEDBACK_ABOUT_ORGANISATION)
    public ResponseEntity<?> deleteFeedbackAboutOrganisation(@RequestParam Long idOfFeedback, Principal principal) {
        return feedbackService.deleteFeedbackAboutOrganisation(idOfFeedback, principal);
    }
}

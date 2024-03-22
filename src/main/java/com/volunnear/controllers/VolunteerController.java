package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.requests.PreferencesRequestDTO;
import com.volunnear.services.activities.RecommendationService;
import com.volunnear.services.users.VolunteerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class VolunteerController {
    private final VolunteerService volunteerService;
    private final RecommendationService recommendationService;

    @GetMapping(value = Routes.GET_VOLUNTEER_PROFILE)
    public ResponseEntity<?> getVolunteerProfile(Principal principal) {
        return volunteerService.getVolunteerProfile(principal);
    }

    @PostMapping(value = Routes.SET_VOLUNTEERS_PREFERENCES)
    public ResponseEntity<?> setVolunteersPreferences(@RequestBody PreferencesRequestDTO preferencesRequestDTO, Principal principal) {
        return volunteerService.setPreferencesForVolunteer(preferencesRequestDTO, principal);
    }

    @GetMapping(value = Routes.GET_RECOMMENDATION_BY_PREFERENCES)
    public ResponseEntity<?> getRecommendationsByPreferencesOfUser(Principal principal) {
        return recommendationService.generateRecommendations(principal);
    }
}
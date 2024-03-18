package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.services.activities.ActivityService;
import com.volunnear.services.users.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class OrganisationController {
    private final ActivityService activityService;
    private final OrganisationService organisationService;

    @GetMapping(value = Routes.GET_ALL_ORGANISATIONS)
    public ResponseEntity<?> getAllOrganisations() {
        return organisationService.getAllOrganisationsWithInfo();
    }

    @GetMapping(value = Routes.GET_ORGANISATION_PROFILE)
    public ResponseEntity<?> getOrganisationProfile(Principal principal){
        return activityService.getMyActivities(principal);
    }
}

package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.requests.AddActivityRequestDTO;
import com.volunnear.services.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @PostMapping(value = Routes.ADD_ACTIVITY, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addActivityFromOrganisation(Principal principal, @RequestBody AddActivityRequestDTO activityRequest) {
        return activityService.addActivityOfOrganisation(activityRequest, principal);
    }

    @GetMapping(value = Routes.MY_ACTIVITIES)
    public ResponseEntity<?> getMyActivities(Principal principal) {
        return activityService.getMyActivities(principal);
    }

    @GetMapping(value = Routes.ACTIVITY_CURRENT_ORGANISATION)
    public ResponseEntity<?> getAllActivitiesOfCurrentOrganisation(@RequestParam String nameOfOrganisation) {
        return activityService.getAllActivitiesFromCurrentOrganisation(nameOfOrganisation);
    }
}

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
    public ResponseEntity<?> addActivityToOrganisation(@RequestBody AddActivityRequestDTO activityRequest, Principal principal) {
        return activityService.addActivityOfOrganisation(activityRequest, principal);
    }

    @GetMapping(value = Routes.GET_MY_ACTIVITIES)
    public ResponseEntity<?> getMyActivities(Principal principal) {
        return activityService.getMyActivities(principal);
    }

    @GetMapping(value = Routes.ACTIVITY_CURRENT_ORGANISATION)
    public ResponseEntity<?> getAllActivitiesOfCurrentOrganisation(@RequestParam String nameOfOrganisation) {
        return activityService.getAllActivitiesFromCurrentOrganisation(nameOfOrganisation);
    }

    @DeleteMapping(value = Routes.DELETE_CURRENT_ACTIVITY_BY_ID)
    public ResponseEntity<?> deleteActivityById(@RequestParam Long id, Principal principal){
        return activityService.deleteActivityById(id, principal);
    }
}

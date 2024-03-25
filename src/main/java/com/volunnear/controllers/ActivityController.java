package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.requests.AddActivityRequestDTO;
import com.volunnear.dtos.requests.NearbyActivitiesRequestDTO;
import com.volunnear.services.activities.ActivityService;
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
        return activityService.addActivityToOrganisation(activityRequest, principal);
    }

    @PostMapping(value = Routes.JOIN_TO_ACTIVITY)
    public ResponseEntity<?> addVolunteerToActivity(@RequestParam Long id, Principal principal) {
        return activityService.addVolunteerToActivity(principal, id);
    }

    @PutMapping(value = Routes.UPDATE_ACTIVITY_INFORMATION, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateActivityInformation(@RequestParam Long idOfActivity,
                                                       @RequestBody AddActivityRequestDTO activityRequestDTO, Principal principal) {
        return activityService.updateActivityInformation(idOfActivity, activityRequestDTO, principal);
    }

    @GetMapping(value = Routes.GET_ALL_ACTIVITIES_WITH_ALL_ORGANISATIONS)
    public ResponseEntity<?> getAllActivitiesOfAllOrganisations() {
        return activityService.getAllActivitiesOfAllOrganisations();
    }

    @GetMapping(value = Routes.GET_MY_ACTIVITIES)
    public ResponseEntity<?> getMyActivities(Principal principal) {
        return activityService.getMyActivities(principal);
    }

    @GetMapping(value = Routes.ACTIVITY_CURRENT_ORGANISATION)
    public ResponseEntity<?> getAllActivitiesOfCurrentOrganisation(@RequestParam String nameOfOrganisation) {
        return activityService.getAllActivitiesFromCurrentOrganisation(nameOfOrganisation);
    }

    @GetMapping(value = Routes.FIND_NEARBY_ACTIVITIES, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findNearbyActivities(@RequestBody NearbyActivitiesRequestDTO nearbyActivitiesRequestDTO) {
        return activityService.findNearbyActivities(nearbyActivitiesRequestDTO);
    }

    @DeleteMapping(value = Routes.DELETE_CURRENT_ACTIVITY_BY_ID)
    public ResponseEntity<?> deleteActivityById(@RequestParam Long id, Principal principal) {
        return activityService.deleteActivityById(id, principal);
    }

    @DeleteMapping(value = Routes.LEAVE_FROM_ACTIVITY_BY_VOLUNTEER)
    public ResponseEntity<?> deleteVolunteerFromActivity(@RequestParam Long id, Principal principal) {
        return activityService.deleteVolunteerFromActivity(id, principal);
    }
}

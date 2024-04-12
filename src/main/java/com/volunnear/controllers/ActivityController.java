package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.requests.AddActivityRequestDTO;
import com.volunnear.dtos.requests.NearbyActivitiesRequestDTO;
import com.volunnear.dtos.response.ActivitiesDTO;
import com.volunnear.services.activities.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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
    public List<ActivitiesDTO> getAllActivitiesOfAllOrganisations() {
        return activityService.getAllActivitiesOfAllOrganisations();
    }

    @GetMapping(value = Routes.GET_MY_ACTIVITIES)
    public ActivitiesDTO getMyActivities(Principal principal) {
        return activityService.getMyActivities(principal);
    }

    @Operation(summary = "Get activities of current organisation", description = "Returns ActivitiesDTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activities dto",
                    content = @Content(schema = @Schema(implementation = ActivitiesDTO.class))),
            @ApiResponse(responseCode = "400", description = "Organisation with name not found")
    })
    @GetMapping(value = Routes.ACTIVITY_CURRENT_ORGANISATION)
    public ResponseEntity<?> getAllActivitiesOfCurrentOrganisation(@RequestParam String nameOfOrganisation) {
        return activityService.getAllActivitiesFromCurrentOrganisation(nameOfOrganisation);
    }

    @Operation(summary = "Get activities nearby", description = "Returns List<ActivitiesDTO>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activities dto",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ActivitiesDTO.class)))),
            @ApiResponse(responseCode = "400", description = "No such activities in current place")
    })
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

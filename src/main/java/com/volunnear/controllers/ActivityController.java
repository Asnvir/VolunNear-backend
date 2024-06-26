package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.requests.AddActivityRequestDTO;
import com.volunnear.dtos.requests.NearbyActivitiesRequestDTO;
import com.volunnear.dtos.response.ActivitiesDTO;
import com.volunnear.dtos.response.ActivityDTO;
import com.volunnear.services.interfaces.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Keycloak")
public class ActivityController {
    private final ActivityService activityService;

    @PostMapping(value = Routes.ADD_ACTIVITY, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addActivityToOrganisation(@RequestBody AddActivityRequestDTO activityRequest, Principal principal) {
        activityService.addActivityToOrganisation(activityRequest, principal);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = Routes.JOIN_TO_ACTIVITY)
    public ResponseEntity<String> addVolunteerToActivity(@RequestParam UUID id, Principal principal) {
        String activityStatus = activityService.addVolunteerToActivity(principal, id);
        return ResponseEntity.ok(activityStatus);
    }

    @PutMapping(value = Routes.UPDATE_ACTIVITY_INFORMATION, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActivityDTO> updateActivityInformation(@RequestParam UUID idOfActivity,
                                                                 @Valid @RequestBody AddActivityRequestDTO activityRequestDTO, Principal principal) {
        ActivityDTO activityDTO = activityService.updateActivityInformation(idOfActivity, activityRequestDTO, principal);
        return ResponseEntity.ok(activityDTO);
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
    public ResponseEntity<ActivitiesDTO> getAllActivitiesOfCurrentOrganisation(@RequestParam String nameOfOrganisation) {
        ActivitiesDTO activitiesDTO = activityService.getAllActivitiesFromCurrentOrganisation(nameOfOrganisation);
        return ResponseEntity.ok(activitiesDTO);
    }

    @Operation(summary = "Get activities nearby", description = "Returns List<ActivitiesDTO>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activities dto",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ActivitiesDTO.class)))),
            @ApiResponse(responseCode = "400", description = "No such activities in current place")
    })
    @GetMapping(value = Routes.FIND_NEARBY_ACTIVITIES, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ActivitiesDTO>> findNearbyActivities(@RequestBody NearbyActivitiesRequestDTO nearbyActivitiesRequestDTO) {
        List<ActivitiesDTO> activitiesDTOS = activityService.findNearbyActivities(nearbyActivitiesRequestDTO);
        return ResponseEntity.ok(activitiesDTOS);
    }

    @DeleteMapping(value = Routes.DELETE_CURRENT_ACTIVITY_BY_ID)
    public ResponseEntity<Void> deleteActivityById(@RequestParam UUID id, Principal principal) {
        activityService.deleteActivityById(id, principal);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = Routes.LEAVE_FROM_ACTIVITY_BY_VOLUNTEER)
    public ResponseEntity<Void> deleteVolunteerFromActivity(@RequestParam UUID id, Principal principal) {
       activityService.deleteVolunteerFromActivity(id, principal);
        return ResponseEntity.ok().build();
    }
}

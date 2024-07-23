package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.enums.ActivityType;
import com.volunnear.dtos.enums.SortOrder;
import com.volunnear.dtos.geoLocation.LocationDTO;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ActivityController {
    private final ActivityService activityService;

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = Routes.ADD_ACTIVITY, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addActivityToOrganisation(@RequestBody AddActivityRequestDTO activityRequest, Principal principal) {
        activityService.addActivityToOrganisation(activityRequest, principal);
        return ResponseEntity.ok().build();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = Routes.JOIN_TO_ACTIVITY)
    public ResponseEntity<String> addVolunteerToActivity(@RequestParam UUID id, Principal principal) {
        String activityStatus = activityService.addVolunteerToActivity(principal, id);
        return ResponseEntity.ok(activityStatus);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping(value = Routes.UPDATE_ACTIVITY_INFORMATION, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActivityDTO> updateActivityInformation(@RequestParam UUID idOfActivity,
                                                                 @RequestBody AddActivityRequestDTO activityRequestDTO, Principal principal) {
        ActivityDTO activityDTO = activityService.updateActivityInformation(idOfActivity, activityRequestDTO, principal);
        return ResponseEntity.ok(activityDTO);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get activities of all organisations by different filters", description = "Returns ActivitiesDTO")
    @GetMapping(value = Routes.GET_ACTIVITIES)
    public List<ActivitiesDTO> getAllActivitiesOfAllOrganisations(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) ActivityType kindOfActivity,
            @RequestParam(required = false) Date dateOfPlace,
            @RequestParam(required = false, defaultValue = "false") boolean myActivities,
            @RequestParam SortOrder sortOrder,
            @RequestParam double latitude,
            @RequestParam double longitude,
            Principal principal
    ) {
        log.info("latitude: {} longitude: {}", latitude, longitude);
        LocationDTO locationDTO = new LocationDTO(latitude, longitude);
        return activityService.getActivities(title, description, country, city, kindOfActivity, dateOfPlace, sortOrder, locationDTO, myActivities, principal);
    }


    @SecurityRequirement(name = "Bearer Authentication")
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

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get activities nearby", description = "Returns List<ActivitiesDTO>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activities dto",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ActivitiesDTO.class)))),
            @ApiResponse(responseCode = "400", description = "No such activities in current place")
    })


    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping(value = Routes.DELETE_CURRENT_ACTIVITY_BY_ID)
    public ResponseEntity<Void> deleteActivityById(@RequestParam UUID id, Principal principal) {
        activityService.deleteActivityById(id, principal);
        return ResponseEntity.ok().build();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping(value = Routes.LEAVE_FROM_ACTIVITY_BY_VOLUNTEER)
    public ResponseEntity<Void> deleteVolunteerFromActivity(@RequestParam UUID id, Principal principal) {
        activityService.deleteVolunteerFromActivity(id, principal);
        return ResponseEntity.ok().build();
    }
}

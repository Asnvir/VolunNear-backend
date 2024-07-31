package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.geoLocation.LocationDTO;
import com.volunnear.dtos.requests.PreferencesRequestDTO;
import com.volunnear.dtos.response.ActivitiesDTO;
import com.volunnear.dtos.response.VolunteerProfileResponseDTO;
import com.volunnear.services.interfaces.ActivityService;
import com.volunnear.services.interfaces.RecommendationService;
import com.volunnear.services.interfaces.VolunteerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class VolunteerController {
    private final VolunteerService volunteerService;
    private final RecommendationService recommendationService;
    private final ActivityService activityService;

    /**
     * Get volunteer profile with all information
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = Routes.GET_VOLUNTEER_PROFILE)
    public VolunteerProfileResponseDTO getVolunteerProfile(Principal principal) {
        return volunteerService.getVolunteerProfile(principal);
    }
    /*
     * @param principal
     * @return List of activities names of current volunteer
     */
    @GetMapping(value = Routes.GET_ALL_ACTIVITIES_OF_CURRENT_VOLUNTEER)
    public ResponseEntity<List<String>> getVolunteerActivitiesNames(Principal principal) {
        List<String> activityNames = activityService.getVolunteersActivityNames(principal);
        return ResponseEntity.ok(activityNames);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Set preferences on user account", description = "Requires volunteer account (token) and preferencesRequestDTO")
    @PostMapping(value = Routes.SET_VOLUNTEERS_PREFERENCES)
    public String setVolunteersPreferences(@RequestBody PreferencesRequestDTO preferencesRequestDTO, Principal principal) {
        return volunteerService.setPreferencesForVolunteer(preferencesRequestDTO, principal);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get recommendations by preferences")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Preferences founded",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ActivitiesDTO.class)))),
            @ApiResponse(responseCode = "400", description = "In your profile no preferences set or Activities by your preferences not founded")
    })
    @GetMapping(value = Routes.GET_RECOMMENDATION_BY_PREFERENCES)
    public ResponseEntity<?> getRecommendationsByPreferencesOfUser(@RequestBody LocationDTO locationDTO, Principal principal) {
        return recommendationService.generateRecommendations(locationDTO, principal);
    }

    @GetMapping(value = Routes.IS_MY_ACTIVITY)
    public ResponseEntity<Boolean> isMyActivity(Principal principal,@RequestParam UUID activityId) {
        return ResponseEntity.ok(volunteerService.isMyActivity(principal, activityId));
    }
}
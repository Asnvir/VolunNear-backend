package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.geoLocation.Location;
import com.volunnear.dtos.response.ActivitiesDTO;
import com.volunnear.dtos.response.OrganisationResponseDTO;
import com.volunnear.services.interfaces.ActivityService;
import com.volunnear.services.interfaces.GeocodingService;
import com.volunnear.services.interfaces.OrganisationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrganisationController {
    private final ActivityService activityService;
    private final OrganisationService organisationService;
    private final GeocodingService geocodingService;

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get all organisations", description = "Returns List<OrganisationResponseDTO>")
    @GetMapping(value = Routes.GET_ALL_ORGANISATIONS)
    public List<OrganisationResponseDTO> getAllOrganisations() {
        return organisationService.getAllOrganisationsWithInfo();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get all organisations", description = "Returns ActivitiesDTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activities dto",
                    content = @Content(schema = @Schema(implementation = ActivitiesDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad token, try again!")
    })
    @GetMapping(value = Routes.GET_ORGANISATION_PROFILE)
    public ActivitiesDTO getOrganisationProfile(Principal principal) {
        return activityService.getMyActivities(principal);
    }
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "/location")
    public Location getLocation(@RequestParam String address) {
        log.info("Getting location for address: {}", address);
        return geocodingService.getCoordinates(address);
    }
}

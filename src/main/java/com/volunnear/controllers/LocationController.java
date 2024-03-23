package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.requests.NearbyActivitiesRequestDTO;
import com.volunnear.services.activities.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping(Routes.FIND_NEARBY_ACTIVITIES)
    public ResponseEntity<?> findNearbyActivities(@RequestBody NearbyActivitiesRequestDTO nearbyActivitiesRequestDTO) {
        return locationService.findNearbyActivities(nearbyActivitiesRequestDTO);
    }
}

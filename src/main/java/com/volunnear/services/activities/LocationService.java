package com.volunnear.services.activities;

import com.volunnear.dtos.requests.NearbyActivitiesRequestDTO;
import com.volunnear.dtos.response.ActivitiesDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final ActivityService activityService;

    public ResponseEntity<?> findNearbyActivities(NearbyActivitiesRequestDTO nearbyActivitiesRequestDTO) {
        List<ActivitiesDTO> activitiesByPlace = activityService.findActivitiesByPlace(nearbyActivitiesRequestDTO.getCountry(), nearbyActivitiesRequestDTO.getCity());
        if (activitiesByPlace.isEmpty()) {
            return new ResponseEntity<>("No such activities in current place", HttpStatus.OK);
        }
        return new ResponseEntity<>(activitiesByPlace, HttpStatus.OK);
    }
}

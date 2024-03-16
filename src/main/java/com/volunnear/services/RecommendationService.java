package com.volunnear.services;

import com.volunnear.dtos.response.ActivitiesDTO;
import com.volunnear.entitiy.users.VolunteerPreference;
import com.volunnear.services.users.VolunteerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final ActivityService activityService;
    private final VolunteerService volunteerService;
    public ResponseEntity<?> generateRecommendations(Principal principal){
        List<String> preferences = learnPreferences(principal);
        if (preferences == null ){
            return new ResponseEntity<>("In your profile no preferences set", HttpStatus.OK);
        }
        List<ActivitiesDTO> organisationsWithActivitiesByPreferences = activityService.getOrganisationsWithActivitiesByPreferences(preferences);
        if (organisationsWithActivitiesByPreferences.isEmpty()){
            return new ResponseEntity<>("Activities by your preferences not founded", HttpStatus.OK);
        }
        return new ResponseEntity<>(organisationsWithActivitiesByPreferences, HttpStatus.OK);
    }

    private List<String> learnPreferences(Principal principal){
        Optional<VolunteerPreference> preferencesOfUser = volunteerService.getPreferencesOfUser(principal);
        return preferencesOfUser.map(VolunteerPreference::getPreferences).orElse(null);
    }
}

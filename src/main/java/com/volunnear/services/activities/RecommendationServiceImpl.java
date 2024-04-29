package com.volunnear.services.activities;

import com.volunnear.dtos.response.ActivitiesDTO;
import com.volunnear.entitiy.infos.VolunteerPreference;
import com.volunnear.services.interfaces.ActivityService;
import com.volunnear.services.interfaces.RecommendationService;
import com.volunnear.services.interfaces.VolunteerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {
    private final ActivityService activityService;
    private final VolunteerService volunteerService;

    @Override
    public ResponseEntity<?> generateRecommendations(Principal principal) {
        List<String> preferences = learnPreferences(principal);
        if (preferences == null) {
            return new ResponseEntity<>("In your profile no preferences set", HttpStatus.BAD_REQUEST);
        }
        List<ActivitiesDTO> organisationsWithActivitiesByPreferences = activityService.getOrganisationsWithActivitiesByPreferences(preferences);
        if (organisationsWithActivitiesByPreferences.isEmpty()) {
            return new ResponseEntity<>("Activities by your preferences not founded", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(organisationsWithActivitiesByPreferences, HttpStatus.OK);
    }

    private List<String> learnPreferences(Principal principal) {
        Optional<VolunteerPreference> preferencesOfUser = volunteerService.getPreferencesOfUser(principal);
        return preferencesOfUser.map(VolunteerPreference::getPreferences).orElse(null);
    }
}

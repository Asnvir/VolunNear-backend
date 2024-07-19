package com.volunnear.services.interfaces;

import com.volunnear.dtos.geoLocation.LocationDTO;
import com.volunnear.dtos.requests.AddActivityRequestDTO;
import com.volunnear.dtos.requests.NearbyActivitiesRequestDTO;
import com.volunnear.dtos.response.ActivitiesDTO;
import com.volunnear.dtos.response.ActivityDTO;
import com.volunnear.entitiy.activities.Activity;
import com.volunnear.entitiy.users.AppUser;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActivityService {
    void addActivityToOrganisation(AddActivityRequestDTO activityRequest, Principal principal);

    void sendNotificationForSubscribers(Activity activity, String status);

    List<ActivitiesDTO> getAllActivitiesOfAllOrganisations();

    List<ActivitiesDTO> getActivities(
            String title,
            String description,
            String country,
            String city,
            String kindOfActivity,
            Date dateOfPlace,
            boolean ascending,
            LocationDTO locationDTO);

    ActivitiesDTO getMyActivities(Principal principal);

    ActivitiesDTO getAllActivitiesFromCurrentOrganisation(String nameOfOrganisation);

    List<ActivitiesDTO> getOrganisationsWithActivitiesByPreferences(List<String> preferences);

    void deleteActivityById(UUID id, Principal principal);

    String addVolunteerToActivity(Principal principal, UUID idOfActivity);

    ActivityDTO updateActivityInformation(UUID idOfActivity, AddActivityRequestDTO activityRequestDTO, Principal principal);

    void deleteVolunteerFromActivity(UUID id, Principal principal);

    List<ActivitiesDTO> getActivitiesOfVolunteer(AppUser appUser);

    List<ActivitiesDTO> findNearbyActivities(NearbyActivitiesRequestDTO nearbyActivitiesRequestDTO);

    Optional<Activity> findActivityByOrganisationAndIdOfActivity(AppUser appUser, UUID idOfActivity);
}

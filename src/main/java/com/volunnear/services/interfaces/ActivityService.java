package com.volunnear.services.interfaces;

import com.volunnear.dtos.enums.ActivityType;
import com.volunnear.dtos.enums.SortOrder;
import com.volunnear.dtos.geoLocation.LocationDTO;
import com.volunnear.dtos.requests.AddActivityRequestDTO;
import com.volunnear.dtos.response.ActivitiesDTO;
import com.volunnear.dtos.response.ActivityDTO;
import com.volunnear.entitiy.activities.Activity;
import com.volunnear.entitiy.users.AppUser;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActivityService {
    Activity addActivityToOrganisation(AddActivityRequestDTO activityRequest, Principal principal);

    void sendNotificationForSubscribers(Activity activity, String status);


    List<ActivitiesDTO> getActivities(
            String title,
            String description,
            String country,
            String city,
            ActivityType kindOfActivity,
            LocalDate dateOfPlace,
            SortOrder sortOrder,
            LocationDTO locationDTO,
            boolean isMyActivities,
            Principal principal
    );

    ActivitiesDTO getOrganisationActivities(Principal principal);

    List<ActivityDTO> getAllActivitiesFromCurrentOrganisation(
            String title,
            String description,
            String country,
            String city,
            ActivityType kindOfActivity,
            LocalDate dateOfPlace,
            SortOrder sortOrder,
            Principal principal
    );

    List<ActivitiesDTO> getOrganisationsWithActivitiesByPreferences(List<ActivityType> preferences, LocationDTO locationDTO);

    void deleteActivityById(UUID id, Principal principal);

    String addVolunteerToActivity(Principal principal, UUID idOfActivity);

    ActivityDTO updateActivityInformation(UUID idOfActivity, AddActivityRequestDTO activityRequestDTO, Principal principal);

    void deleteVolunteerFromActivity(UUID id, Principal principal);

    List<ActivitiesDTO> getActivitiesOfVolunteer(AppUser appUser);


    Optional<Activity> findActivityByOrganisationAndIdOfActivity(AppUser appUser, UUID idOfActivity);

    List<String> getAllActivityNames();

    public List<String> getVolunteersActivityNames(Principal principal);
}

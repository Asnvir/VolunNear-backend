package com.volunnear.services.activities;

import com.volunnear.dtos.ActivityNotificationDTO;
import com.volunnear.dtos.geoLocation.LocationDTO;
import com.volunnear.dtos.requests.AddActivityRequestDTO;
import com.volunnear.dtos.requests.GetActivitiesRequestDTO;
import com.volunnear.dtos.requests.NearbyActivitiesRequestDTO;
import com.volunnear.dtos.response.ActivitiesDTO;
import com.volunnear.dtos.response.ActivityDTO;
import com.volunnear.dtos.response.OrganisationResponseDTO;
import com.volunnear.entitiy.activities.Activity;
import com.volunnear.entitiy.activities.VolunteerInActivity;
import com.volunnear.entitiy.infos.OrganisationInfo;
import com.volunnear.entitiy.users.AppUser;
import com.volunnear.events.ActivityCreationEvent;
import com.volunnear.exceptions.BadRequestException;
import com.volunnear.exceptions.NotFoundException;
import com.volunnear.exceptions.activity.ActivityNotFoundException;
import com.volunnear.exceptions.activity.AuthErrorException;
import com.volunnear.mappers.ActivityMapper;
import com.volunnear.repositories.infos.ActivitiesRepository;
import com.volunnear.repositories.infos.VolunteersInActivityRepository;
import com.volunnear.services.interfaces.ActivityService;
import com.volunnear.services.interfaces.GeocodingService;
import com.volunnear.services.interfaces.OrganisationService;
import com.volunnear.services.users.UserService;
import com.volunnear.specification.ActivitySpecification;
import com.volunnear.utils.DistanceCalculator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    private final UserService userService;
    private final OrganisationService organisationService;
    private final ApplicationEventPublisher eventPublisher;
    private final ActivitiesRepository activitiesRepository;
    private final VolunteersInActivityRepository volunteersInActivityRepository;
    private final ActivityMapper activityMapper;
    private final GeocodingService geocodingService;

    @Override
    public void addActivityToOrganisation(AddActivityRequestDTO activityRequest, Principal principal) {
        AppUser organisation = userService.findAppUserByUsername(principal.getName())
                .orElseThrow(() -> new AuthErrorException("Incorrect token data about organisation"));
        if (!organisationService.isUserAreOrganisation(organisation)) {
            throw new AuthErrorException("You are not organisation");
        }
        Activity activity = new Activity();
        LocationDTO locationDTO = geocodingService.getCoordinates(activityRequest.getCity(), activityRequest.getStreet(), activityRequest.getHouseNumber());
        activity.setTitle(activityRequest.getTitle());
        activity.setDescription(activityRequest.getDescription());
        activity.setCountry(activityRequest.getCountry());
        activity.setCity(activityRequest.getCity());
        activity.setDateOfPlace(new Date());
        activity.setKindOfActivity(activityRequest.getKindOfActivity());
        activity.setLatitude(locationDTO.latitude());
        activity.setLongitude(locationDTO.longitude());
        activity.setAppUser(organisation);
        activitiesRepository.save(activity);

        sendNotificationForSubscribers(activity, "New");
    }

    @Override
    @Async
    public void sendNotificationForSubscribers(Activity activity, String status) {
        ActivityNotificationDTO notificationDTO = new ActivityNotificationDTO();
        ActivityDTO activityDTO = new ActivityDTO();

        activityDTO.setCity(activity.getCity());
        activityDTO.setCountry(activity.getCountry());
        activityDTO.setKindOfActivity(activity.getKindOfActivity());
        activityDTO.setTitle(activity.getTitle());
        activityDTO.setDescription(activity.getDescription());
        activityDTO.setDateOfPlace(activity.getDateOfPlace());

        notificationDTO.setActivityDTO(activityDTO);
        notificationDTO.setOrganisationResponseDTO(getOrganisationResponseDTO(organisationService.findAdditionalInfoAboutOrganisation(activity.getAppUser())));
        eventPublisher.publishEvent(new ActivityCreationEvent(this, notificationDTO, status));
    }

    @Override
    public List<ActivitiesDTO> getAllActivitiesOfAllOrganisations() {
        List<Activity> allActivities = activitiesRepository.findAll();

        return getListOfActivitiesDTOForResponse(allActivities);
    }

    /**
     * Get Activities by title, description, country, city, kindOfActivity, dateOfPlace
     */
    @Override
    public List<ActivitiesDTO> getActivities(GetActivitiesRequestDTO getActivitiesRequestDTO, LocationDTO locationDTO) {
        Specification<Activity> spec = Specification.where(ActivitySpecification.hasTitle(getActivitiesRequestDTO.getTitle()))
                .and(ActivitySpecification.hasDescription(getActivitiesRequestDTO.getDescription()))
                .and(ActivitySpecification.hasCountry(getActivitiesRequestDTO.getCountry()))
                .and(ActivitySpecification.hasCity(getActivitiesRequestDTO.getCity()))
                .and(ActivitySpecification.hasKindOfActivity(getActivitiesRequestDTO.getKindOfActivity()))
                .and(ActivitySpecification.hasDateOfPlace(getActivitiesRequestDTO.getDateOfPlace()));
        List<Activity> allActivities = activitiesRepository.findAll(spec);

        return getSortedListOfActivitiesDTOByDistance(allActivities, locationDTO, getActivitiesRequestDTO.isAscending());
    }

    /**
     * Activities by organisation username from token
     */
    @Override
    public ActivitiesDTO getMyActivities(Principal principal) {
        String username = principal.getName();
        Optional<AppUser> appUserByUsername = userService.findAppUserByUsername(username);
        OrganisationInfo additionalInfoAboutOrganisation = organisationService.findAdditionalInfoAboutOrganisation(appUserByUsername.get());
        List<Activity> activitiesByAppUser = activitiesRepository.findActivitiesByAppUser(appUserByUsername.get());

        return activitiesFromEntityToDto(additionalInfoAboutOrganisation, activitiesByAppUser);
    }

    /**
     * All activities of current organisation by organisation name
     */
    @Override
    public ActivitiesDTO getAllActivitiesFromCurrentOrganisation(String nameOfOrganisation) {
        Optional<OrganisationInfo> organisationByNameOfOrganisation = organisationService.findOrganisationByNameOfOrganisation(nameOfOrganisation);
        OrganisationInfo organisationInfo = organisationByNameOfOrganisation.orElseThrow(
                () -> new NotFoundException("Organisation with name " + nameOfOrganisation + " not found")
        );

        List<Activity> activitiesByAppUser = activitiesRepository.findActivitiesByAppUser(organisationInfo.getAppUser());
        return activitiesFromEntityToDto(organisationInfo, activitiesByAppUser);
    }

    @Override
    public List<ActivitiesDTO> getOrganisationsWithActivitiesByPreferences(List<String> preferences) {
        List<Activity> activityByKindOfActivity = activitiesRepository.findActivityByKindOfActivityIgnoreCaseIn(preferences);
        return getListOfActivitiesDTOForResponse(activityByKindOfActivity);
    }


    /**
     * Delete activity by id and from org principal (organisation data)
     */
    @Override
    @SneakyThrows
    public void deleteActivityById(UUID id, Principal principal) {
        AppUser appUser = organisationService.findOrganisationByUsername(principal.getName()).get();
        Optional<Activity> activityById = activitiesRepository.findById(id);

        if (activityById.isEmpty() || !appUser.equals(activityById.get().getAppUser())) {
            throw new BadRequestException("Bad id of activity!");
        }

        activitiesRepository.deleteById(id);
    }

    @Override
    public String addVolunteerToActivity(Principal principal, UUID idOfActivity) {
        AppUser appUser = userService.findAppUserByUsername(principal.getName())
                .orElseThrow(() -> new AuthErrorException("Incorrect token data about volunteer"));
        List<VolunteerInActivity> allByUser = volunteersInActivityRepository.findAllByUser(appUser);

//        if (allByUser.size() > 5) {
//            return new ResponseEntity<>("To much activities in yours profile!", HttpStatus.OK);
//        }

        Activity activityById = activitiesRepository.findById(idOfActivity)
                .orElseThrow(() -> new ActivityNotFoundException("Activity with id " + idOfActivity + " not found"));
        VolunteerInActivity volunteerInActivity = new VolunteerInActivity();
        volunteerInActivity.setUser(appUser);
        volunteerInActivity.setActivity(activityById);
        volunteersInActivityRepository.save(volunteerInActivity);
        return activityById.getTitle();
    }

    @Override
    public ActivityDTO updateActivityInformation(UUID idOfActivity, AddActivityRequestDTO activityRequestDTO, Principal principal) {
        AppUser appUser = userService.findAppUserByUsername(principal.getName()).get();
        Optional<Activity> activityById = activitiesRepository.findById(idOfActivity);
        if (activityById.isEmpty() || !appUser.equals(activityById.get().getAppUser())) {
            throw new BadRequestException("Bad id of activity!");
        }

        Activity activity = activityById.get();

        activity.setTitle(activityRequestDTO.getTitle());
        activity.setDescription(activityRequestDTO.getDescription());
        activity.setCountry(activityRequestDTO.getCountry());
        activity.setCity(activityRequestDTO.getCity());
        activity.setDateOfPlace(new Date());
        activity.setKindOfActivity(activityRequestDTO.getKindOfActivity());
        Activity updatedActivity = activitiesRepository.save(activity);
        sendNotificationForSubscribers(activity, "Updated");

        return activityMapper.activityToActivityDTO(updatedActivity);
    }

    @Transactional
    @Override
    public void deleteVolunteerFromActivity(UUID id, Principal principal) {
        AppUser appUser = userService.findAppUserByUsername(principal.getName()).get();
        if (!volunteersInActivityRepository.existsByUserAndActivity_Id(appUser, id)) {
            throw new BadRequestException("You are not in this activity!");
        }
        volunteersInActivityRepository.deleteByActivity_IdAndUser_Id(id, appUser.getId());
    }

    @Override
    public List<ActivitiesDTO> getActivitiesOfVolunteer(AppUser appUser) {
        List<VolunteerInActivity> allByUser = volunteersInActivityRepository.findAllByUser(appUser);
        List<Activity> infoAboutActivities = allByUser.stream().map(volunteerInActivity ->
                activitiesRepository.findById(volunteerInActivity.getActivity().getId()).get()).toList();
        return getListOfActivitiesDTOForResponse(infoAboutActivities);
    }

    @Override
    public List<ActivitiesDTO> findNearbyActivities(NearbyActivitiesRequestDTO nearbyActivitiesRequestDTO) {
        return getListOfActivitiesDTOForResponse(
                activitiesRepository.findActivityByCountryAndCity(nearbyActivitiesRequestDTO.getCountry(), nearbyActivitiesRequestDTO.getCity()));
    }

    @Override
    public Optional<Activity> findActivityByOrganisationAndIdOfActivity(AppUser appUser, UUID idOfActivity) {
        return activitiesRepository.findActivityByAppUserAndId(appUser, idOfActivity);
    }

    /**
     * Methods to convert entities from DB to DTO for response
     */
    private static ActivitiesDTO activitiesFromEntityToDto(OrganisationInfo additionalInfoAboutOrganisation, List<Activity> activitiesByAppUser) {
        ActivitiesDTO activitiesDTO = new ActivitiesDTO();

        OrganisationResponseDTO responseDTO = getOrganisationResponseDTO(additionalInfoAboutOrganisation);

        for (Activity activity : activitiesByAppUser) {
            activitiesDTO.addActivity(new ActivityDTO(activity.getId(),
                    activity.getCity(),
                    activity.getCountry(),
                    activity.getTitle(),
                    activity.getDescription(),
                    activity.getKindOfActivity(),
                    activity.getDateOfPlace(),
                    new LocationDTO(activity.getLatitude(), activity.getLongitude()),
                    0.0
            ));
        }

        activitiesDTO.setOrganisationResponseDTO(responseDTO);
        return activitiesDTO;
    }

    private static OrganisationResponseDTO getOrganisationResponseDTO(OrganisationInfo additionalInfoAboutOrganisation) {
        return new OrganisationResponseDTO(
                additionalInfoAboutOrganisation.getAppUser().getId(),
                additionalInfoAboutOrganisation.getNameOfOrganisation(),
                additionalInfoAboutOrganisation.getCountry(),
                additionalInfoAboutOrganisation.getCity(),
                additionalInfoAboutOrganisation.getAddress());
    }


    private List<ActivitiesDTO> getListOfActivitiesDTOForResponse(List<Activity> activities) {
        List<ActivitiesDTO> responseActivities = new ArrayList<>();

        Map<AppUser, List<Activity>> activitiesByOrganisationMap = activities.stream()
                .collect(Collectors.groupingBy(Activity::getAppUser));

        for (Map.Entry<AppUser, List<Activity>> organisationWithActivity : activitiesByOrganisationMap.entrySet()) {
            responseActivities.add(activitiesFromEntityToDto(organisationService.findAdditionalInfoAboutOrganisation(organisationWithActivity.getKey()),
                    organisationWithActivity.getValue()));
        }
        return responseActivities;
    }

    public List<ActivitiesDTO> getSortedListOfActivitiesDTOByDistance(List<Activity> activities, LocationDTO locationDTO, boolean sortAscending) {
        List<ActivitiesDTO> responseActivities = new ArrayList<>();

        Map<AppUser, List<Activity>> activitiesByOrganisationMap = activities.stream()
                .collect(Collectors.groupingBy(Activity::getAppUser));

        for (Map.Entry<AppUser, List<Activity>> organisationWithActivity : activitiesByOrganisationMap.entrySet()) {
            ActivitiesDTO activitiesDTO = activitiesFromEntityToDto(organisationService.findAdditionalInfoAboutOrganisation(organisationWithActivity.getKey()),
                    organisationWithActivity.getValue());
            List<ActivityDTO> sortedActivities = sortActivitiesByDistance(organisationWithActivity.getValue(), locationDTO, sortAscending);
            activitiesDTO.setActivities(sortedActivities);
            responseActivities.add(activitiesDTO);
        }
        return responseActivities;
    }

    private List<ActivityDTO> sortActivitiesByDistance(List<Activity> activities, LocationDTO locationDTO, boolean sortAscending) {
        return activities.stream()
                .map(activity -> new ActivityDTO(activity.getId(), activity.getTitle(), activity.getDescription(),
                        activity.getCountry(), activity.getCity(), activity.getKindOfActivity(), activity.getDateOfPlace(),
                        new LocationDTO(activity.getLatitude(), activity.getLongitude()),
                        DistanceCalculator.calculateDistance(locationDTO.latitude(), locationDTO.longitude(),
                                activity.getLatitude(), activity.getLongitude())))
                .sorted((a1, a2) -> sortAscending
                        ? Double.compare(a1.getDistance(), a2.getDistance())
                        : Double.compare(a2.getDistance(), a1.getDistance()))
                .collect(Collectors.toList());
    }

}

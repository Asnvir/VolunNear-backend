package com.volunnear.services.activities;

import com.volunnear.dtos.ActivityNotificationDTO;
import com.volunnear.dtos.enums.ActivityType;
import com.volunnear.dtos.enums.SortOrder;
import com.volunnear.dtos.geoLocation.LocationDTO;
import com.volunnear.dtos.requests.AddActivityRequestDTO;
import com.volunnear.dtos.response.ActivitiesDTO;
import com.volunnear.dtos.response.ActivityDTO;
import com.volunnear.dtos.response.OrganisationResponseDTO;
import com.volunnear.entitiy.GalleryImage;
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
import com.volunnear.specification.SpecificationEnricher;
import com.volunnear.utils.DateUtils;
import com.volunnear.utils.DistanceCalculator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.time.LocalDate;
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
    private final SpecificationEnricher specificationEnricher;

    @Override
    public Activity addActivityToOrganisation(AddActivityRequestDTO activityRequest, Principal principal) {
        AppUser organisation = userService.findAppUserByUsername(principal.getName())
                .orElseThrow(() -> new AuthErrorException("Incorrect token data about organisation"));
        if (!organisationService.isUserAreOrganisation(organisation)) {
            throw new AuthErrorException("You are not organisation");
        }

        Date date = DateUtils.convertStringToDate(activityRequest.getDateOfPlace());

        Activity activity = new Activity();
        LocationDTO locationDTO = geocodingService.getCoordinates(activityRequest.getCity(), activityRequest.getStreet(), activityRequest.getHouseNumber());
        activity.setTitle(activityRequest.getTitle());
        activity.setDescription(activityRequest.getDescription());
        activity.setCountry(activityRequest.getCountry());
        activity.setCity(activityRequest.getCity());
        activity.setStreet(activityRequest.getStreet());
        activity.setNumberOfHouse(activityRequest.getHouseNumber());
        activity.setDateOfPlace(date);
        activity.setKindOfActivity(activityRequest.getKindOfActivity());
        activity.setLatitude(locationDTO.getLatitude());
        activity.setLongitude(locationDTO.getLongitude());
        activity.setAppUser(organisation);
        Activity createdActivity = activitiesRepository.save(activity);

        sendNotificationForSubscribers(activity, "New");
        return createdActivity;
    }

    @Override
    @Async
    public void sendNotificationForSubscribers(Activity activity, String status) {
        ActivityNotificationDTO notificationDTO = new ActivityNotificationDTO();
        ActivityDTO activityDTO = getActivityDTO(activity);

        notificationDTO.setActivityDTO(activityDTO);
        notificationDTO.setOrganisationResponseDTO(getOrganisationResponseDTO(organisationService.findAdditionalInfoAboutOrganisation(activity.getAppUser())));
        eventPublisher.publishEvent(new ActivityCreationEvent(this, notificationDTO, status));
    }

    @NotNull
    private static ActivityDTO getActivityDTO(Activity activity) {
        ActivityDTO activityDTO = new ActivityDTO();

        activityDTO.setCity(activity.getCity());
        activityDTO.setCountry(activity.getCountry());
        activityDTO.setKindOfActivity(activity.getKindOfActivity());
        activityDTO.setTitle(activity.getTitle());
        activityDTO.setDescription(activity.getDescription());
        activityDTO.setDateOfPlace(activity.getDateOfPlace());
        activityDTO.setLocationDTO(new LocationDTO(activity.getLatitude(), activity.getLongitude()));
        activityDTO.setCoverImage(activity.getCoverImageUrl());
        List<GalleryImage> galleryImages = activity.getGalleryImages();
        if (galleryImages != null) {
            activityDTO.setGalleryImages(galleryImages.stream().map(GalleryImage::getImageUrl).collect(Collectors.toList()));
        } else {
            activityDTO.setGalleryImages(Collections.emptyList());
        }
        return activityDTO;
    }


    /**
     * Get Activities by title, description, country, city, kindOfActivity, dateOfPlace
     */
    @Override
    public Page<ActivitiesDTO> getActivities(
            String title,
            String description,
            String country,
            String city,
            ActivityType kindOfActivity,
            LocalDate dateOfPlace,
            SortOrder sortOrder,
            LocationDTO locationDTO,
            boolean isMyActivities,
            Pageable pageable,
            Principal principal) {
        Page<Activity> activitiesPage;
        if (isMyActivities) {
            AppUser appUser = userService.findAppUserByUsername(principal.getName()).get();
            Specification<VolunteerInActivity> spec = specificationEnricher
                    .createVolunteerInActivitySpecification(
                            title, description, country, city, kindOfActivity, dateOfPlace, appUser
                    );

            activitiesPage = volunteersInActivityRepository
                    .findAll(spec,pageable)
                    .map(VolunteerInActivity::getActivity);
        } else {
            Specification<Activity> spec = specificationEnricher
                    .createSpecification(
                            title, description, country, city, kindOfActivity, dateOfPlace
                    );
            activitiesPage = activitiesRepository.findAll(spec, pageable);
        }

        List<ActivitiesDTO> activitiesDTOs = getSortedListOfActivitiesDTOByDistance(activitiesPage.getContent(), locationDTO, sortOrder);
        return new PageImpl<>(activitiesDTOs, pageable, activitiesPage.getTotalElements());
    }

    /**
     * Activities by organisation username from token
     */
    @Override
    public ActivitiesDTO getOrganisationActivities(Principal principal) {
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
    public List<ActivityDTO> getAllActivitiesFromCurrentOrganisation(String title,
                                                                     String description,
                                                                     String country,
                                                                     String city,
                                                                     ActivityType kindOfActivity,
                                                                     LocalDate dateOfPlace,
                                                                     SortOrder sortOrder,
                                                                     Principal principal)
                                                                  {
                                                                      AppUser appUser = userService.findAppUserByUsername(principal.getName()).get();
        Specification<Activity> spec = specificationEnricher.createSpecification(title, description, country, city, kindOfActivity, dateOfPlace);
        spec.and(Specification.where(ActivitySpecification.hasAppUser(appUser)));

        List<Activity> activitiesByAppUser = activitiesRepository.findAll(spec);
        return activitiesToActivitiesDTO(activitiesByAppUser);
    }

    /**
     * Get organisations with activities by preferences
     */
    @Override
    public List<ActivitiesDTO> getOrganisationsWithActivitiesByPreferences(List<ActivityType> preferences, LocationDTO locationDTO) {
        List<Activity> activityByKindOfActivity = activitiesRepository.findByKindOfActivityIn(preferences);
        return getSortedListOfActivitiesDTOByDistance(activityByKindOfActivity, locationDTO, SortOrder.ASC);
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

    /**
     * Add volunteer to activity by id
     */
    @Override
    public String addVolunteerToActivity(Principal principal, UUID idOfActivity) {
        AppUser appUser = userService.findAppUserByUsername(principal.getName())
                .orElseThrow(() -> new AuthErrorException("Incorrect token data about volunteer"));
        List<VolunteerInActivity> allByUser = volunteersInActivityRepository.findAllByUser(appUser);
        Activity activityById = activitiesRepository.findById(idOfActivity)
                .orElseThrow(() -> new ActivityNotFoundException("Activity with id " + idOfActivity + " not found"));
        VolunteerInActivity volunteerInActivity = new VolunteerInActivity();
        volunteerInActivity.setUser(appUser);
        volunteerInActivity.setActivity(activityById);
        volunteersInActivityRepository.save(volunteerInActivity);
        return activityById.getTitle();
    }

    /**
     * Update activity information by id
     */
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

    /**
     * Delete volunteer from activity by id
     */
    @Transactional
    @Override
    public void deleteVolunteerFromActivity(UUID id, Principal principal) {
        AppUser appUser = userService.findAppUserByUsername(principal.getName()).get();
        if (!volunteersInActivityRepository.existsByUserAndActivity_Id(appUser, id)) {
            throw new BadRequestException("You are not in this activity!");
        }
        volunteersInActivityRepository.deleteByActivity_IdAndUser_Id(id, appUser.getId());
    }

    /**
     * Get activities of volunteer by user
     */
    @Override
    public List<ActivitiesDTO> getActivitiesOfVolunteer(AppUser appUser) {
        List<VolunteerInActivity> allByUser = volunteersInActivityRepository.findAllByUser(appUser);
        List<Activity> infoAboutActivities = allByUser.stream().map(volunteerInActivity ->
                activitiesRepository.findById(volunteerInActivity.getActivity().getId()).get()).toList();
        return getListOfActivitiesDTOForResponse(infoAboutActivities);
    }

    /**
     * Find activity by organisation and id of activity
     */
    @Override
    public Optional<Activity> findActivityByOrganisationAndIdOfActivity(AppUser appUser, UUID idOfActivity) {
        return activitiesRepository.findActivityByAppUserAndId(appUser, idOfActivity);
    }

    /**
     * Get volunteer activity names by  principal
     */
    @Override
    public List<String> getVolunteersActivityNames(Principal principal) {
        List<VolunteerInActivity> volunteerInActivities = volunteersInActivityRepository.findAllByUser(userService.findAppUserByUsername(principal.getName()).get());
        return volunteerInActivities.stream().map(volunteerInActivity -> volunteerInActivity.getActivity().getTitle()).collect(Collectors.toList());
    }

    /**
     * Get all activity names
     */
    @Override
    public List<String> getAllActivityNames() {
        List<Activity> activities = activitiesRepository.findAll();
        return activities.stream().map(Activity::getTitle).collect(Collectors.toList());
    }

    /**
     * Methods to convert entities from DB to DTO for response
     */
    private ActivitiesDTO activitiesFromEntityToDto(OrganisationInfo additionalInfoAboutOrganisation, List<Activity> activitiesByAppUser) {
        ActivitiesDTO activitiesDTO = new ActivitiesDTO();

        OrganisationResponseDTO responseDTO = getOrganisationResponseDTO(additionalInfoAboutOrganisation);

        for (Activity activity : activitiesByAppUser) {
            activitiesDTO.addActivity(new ActivityDTO(activity.getId(),
                    activity.getCity(),
                    activity.getCountry(),
                    activity.getStreet(),
                    activity.getNumberOfHouse(),
                    activity.getTitle(),
                    activity.getDescription(),
                    activity.getKindOfActivity(),
                    activity.getDateOfPlace(),
                    new LocationDTO(activity.getLatitude(), activity.getLongitude()),
                    0.0,
                    activity.getCoverImageUrl(),
                    activity.getGalleryImages().stream().map(GalleryImage::getImageUrl).collect(Collectors.toList())
            ));
        }

        activitiesDTO.setOrganisationResponseDTO(responseDTO);
        return activitiesDTO;
    }

    private List<ActivityDTO> activitiesToActivitiesDTO(List<Activity> activities) {
        List<ActivityDTO> activitiesDTO = new ArrayList<>();
        for (Activity activity : activities) {
            activitiesDTO.add(new ActivityDTO(activity.getId(),
                    activity.getTitle(),
                    activity.getDescription(),
                    activity.getCountry(),
                    activity.getCity(),
                    activity.getStreet(),
                    activity.getNumberOfHouse(),
                    activity.getKindOfActivity(),
                    activity.getDateOfPlace(),
                    new LocationDTO(activity.getLatitude(), activity.getLongitude()),
                    0.0,
                    activity.getCoverImageUrl(),
                    activity.getGalleryImages().stream().map(GalleryImage::getImageUrl).collect(Collectors.toList())
            ));
        }
        return activitiesDTO;
    }

    private OrganisationResponseDTO getOrganisationResponseDTO(OrganisationInfo additionalInfoAboutOrganisation) {
        return new OrganisationResponseDTO(
                additionalInfoAboutOrganisation.getAppUser().getId(),
                additionalInfoAboutOrganisation.getNameOfOrganisation(),
                additionalInfoAboutOrganisation.getCountry(),
                additionalInfoAboutOrganisation.getCity(),
                additionalInfoAboutOrganisation.getAddress(),
                additionalInfoAboutOrganisation.getAvatarUrl(),
                additionalInfoAboutOrganisation.getAppUser().getEmail(),
                additionalInfoAboutOrganisation.getAppUser().getUsername(),
                additionalInfoAboutOrganisation.getAverageRating()
        );
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

    public List<ActivitiesDTO> getSortedListOfActivitiesDTOByDistance(List<Activity> activities, LocationDTO locationDTO, SortOrder sortOrder) {
        List<ActivitiesDTO> responseActivities = new ArrayList<>();

        Map<AppUser, List<Activity>> activitiesByOrganisationMap = activities.stream()
                .collect(Collectors.groupingBy(Activity::getAppUser));

        for (Map.Entry<AppUser, List<Activity>> organisationWithActivity : activitiesByOrganisationMap.entrySet()) {
            ActivitiesDTO activitiesDTO = activitiesFromEntityToDto(organisationService.findAdditionalInfoAboutOrganisation(organisationWithActivity.getKey()),
                    organisationWithActivity.getValue());
            List<ActivityDTO> sortedActivities = sortActivitiesByDistance(organisationWithActivity.getValue(), locationDTO, sortOrder);
            activitiesDTO.setActivities(sortedActivities);
            responseActivities.add(activitiesDTO);
        }
        return responseActivities;
    }

    private List<ActivityDTO> sortActivitiesByDistance(List<Activity> activities, LocationDTO locationDTO, SortOrder sortOrder) {
        return activities.stream()
                .map(activity -> new ActivityDTO(activity.getId(), activity.getTitle(), activity.getDescription(),
                                activity.getCountry(), activity.getCity(), activity.getStreet(), activity.getNumberOfHouse(), activity.getKindOfActivity(), activity.getDateOfPlace(),
                                new LocationDTO(activity.getLatitude(), activity.getLongitude()),
                                DistanceCalculator.calculateDistance(locationDTO.getLatitude(), locationDTO.getLongitude(),
                                        activity.getLatitude(), activity.getLongitude()),
                                activity.getCoverImageUrl(),
                                activity.getGalleryImages().stream().map(GalleryImage::getImageUrl).collect(Collectors.toList()
                                )
                        )
                )
                .sorted((a1, a2) -> sortOrder == SortOrder.ASC
                        ? Double.compare(a1.getDistance(), a2.getDistance())
                        : Double.compare(a2.getDistance(), a1.getDistance()))
                .collect(Collectors.toList());
    }

}

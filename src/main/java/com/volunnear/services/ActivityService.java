package com.volunnear.services;

import com.volunnear.dtos.requests.AddActivityRequestDTO;
import com.volunnear.dtos.response.ActivitiesDTO;
import com.volunnear.dtos.response.ActivityDTO;
import com.volunnear.dtos.response.OrganisationResponseDTO;
import com.volunnear.entitiy.activities.Activity;
import com.volunnear.entitiy.users.AppUser;
import com.volunnear.entitiy.users.OrganisationInfo;
import com.volunnear.exceptions.AuthErrorException;
import com.volunnear.repositories.ActivitiesRepository;
import com.volunnear.security.jwt.JwtTokenProvider;
import com.volunnear.services.users.OrganisationService;
import com.volunnear.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final UserService userService;
    private final OrganisationService organisationService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ActivitiesRepository activitiesRepository;

    public ResponseEntity<?> addActivityOfOrganisation(AddActivityRequestDTO activityRequest, Principal principal) {
        String usernameFromToken = jwtTokenProvider.getUsernameFromToken(principal.getName());
        Optional<AppUser> organisation = userService.findAppUserByUsername(usernameFromToken);
        if (organisation.isEmpty()) {
            return new ResponseEntity<>(new AuthErrorException(HttpStatus.UNAUTHORIZED.value(), "Incorrect token data about organisation"), HttpStatus.UNAUTHORIZED);
        }
        Activity activity = new Activity();
        activity.setTitle(activityRequest.getTitle());
        activity.setDescription(activityRequest.getDescription());
        activity.setCountry(activityRequest.getCountry());
        activity.setCity(activityRequest.getCity());
        activity.setDateOfPlace(new Date());
        activity.setKindOfActivity(activityRequest.getKindOfActivity());
        activity.setAppUser(organisation.get());
        activitiesRepository.save(activity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Activities by organisation username from token
     */
    public ResponseEntity<?> getMyActivities(Principal principal) {
        String username = principal.getName();
        Optional<AppUser> appUserByUsername = userService.findAppUserByUsername(username);
        if (appUserByUsername.isEmpty()) {
            return new ResponseEntity<>("Bad token, try again!", HttpStatus.BAD_REQUEST);
        }
        OrganisationInfo additionalInfoAboutOrganisation = organisationService.findAdditionalInfoAboutOrganisation(appUserByUsername.get());
        List<Activity> activitiesByAppUser = activitiesRepository.findActivitiesByAppUser(appUserByUsername.get());

        ActivitiesDTO activitiesDTO = activitiesFromEntityToDto(additionalInfoAboutOrganisation, activitiesByAppUser);

        return new ResponseEntity<>(activitiesDTO, HttpStatus.OK);
    }

    /**
     * All activities of current organisation by organisation name
     */
    public ResponseEntity<?> getAllActivitiesFromCurrentOrganisation(String nameOfOrganisation) {
        Optional<OrganisationInfo> organisationByNameOfOrganisation = organisationService.findOrganisationByNameOfOrganisation(nameOfOrganisation);
        if (organisationByNameOfOrganisation.isEmpty()) {
            return new ResponseEntity<>("Organisation with name " + nameOfOrganisation + " not found", HttpStatus.BAD_REQUEST);
        }
        OrganisationInfo organisationInfo = organisationByNameOfOrganisation.get();

        List<Activity> activitiesByAppUser = activitiesRepository.findActivitiesByAppUser(organisationInfo.getAppUser());
        ActivitiesDTO activitiesDTO = activitiesFromEntityToDto(organisationInfo, activitiesByAppUser);

        return new ResponseEntity<>(activitiesDTO, HttpStatus.OK);
    }

    /**
     * Methods to convert entities from DB to DTO for response
     */
    private static ActivitiesDTO activitiesFromEntityToDto(OrganisationInfo additionalInfoAboutOrganisation, List<Activity> activitiesByAppUser) {
        ActivitiesDTO activitiesDTO = new ActivitiesDTO();

        OrganisationResponseDTO responseDTO = getOrganisationResponseDTO(additionalInfoAboutOrganisation);

        for (Activity activity : activitiesByAppUser) {
            activitiesDTO.addActivity(new ActivityDTO(activity.getCity(),
                    activity.getCountry(),
                    activity.getDateOfPlace(),
                    activity.getDescription(),
                    activity.getTitle(),
                    activity.getKindOfActivity()));
        }

        activitiesDTO.setOrganisationResponseDTO(responseDTO);
        return activitiesDTO;
    }

    private static OrganisationResponseDTO getOrganisationResponseDTO(OrganisationInfo additionalInfoAboutOrganisation) {
        return new OrganisationResponseDTO(
                additionalInfoAboutOrganisation.getNameOfOrganisation(),
                additionalInfoAboutOrganisation.getCountry(),
                additionalInfoAboutOrganisation.getCity(),
                additionalInfoAboutOrganisation.getAddress(),
                additionalInfoAboutOrganisation.getIndustry());
    }
}

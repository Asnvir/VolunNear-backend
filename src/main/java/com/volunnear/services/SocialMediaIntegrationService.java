package com.volunnear.services;

import com.volunnear.entitiy.activities.Activity;
import com.volunnear.entitiy.infos.ActivityChatLink;
import com.volunnear.entitiy.infos.OrganisationGroupLink;
import com.volunnear.entitiy.infos.OrganisationInfo;
import com.volunnear.entitiy.users.AppUser;
import com.volunnear.repositories.infos.ActivityChatLinkRepository;
import com.volunnear.repositories.infos.OrganisationGroupLinkRepository;
import com.volunnear.services.activities.ActivityService;
import com.volunnear.services.users.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SocialMediaIntegrationService {
    private final ActivityService activityService;
    private final OrganisationService organisationService;
    private final ActivityChatLinkRepository activityChatLinkRepository;
    private final OrganisationGroupLinkRepository organisationGroupLinkRepository;

    public ResponseEntity<?> addChatLinkToActivity(Long idOfActivity, String link, Principal principal) {
        Optional<AppUser> organisationByUsername = organisationService.findOrganisationByUsername(principal.getName());
        Optional<Activity> activity = activityService.findActivityByOrganisationAndIdOfActivity(organisationByUsername.get(), idOfActivity);
        if (activity.isEmpty()) {
            return new ResponseEntity<>("Bad id of activity!", HttpStatus.BAD_REQUEST);
        } else if (activityChatLinkRepository.existsByActivity_Id(idOfActivity)) {
            return new ResponseEntity<>("Link already exists", HttpStatus.BAD_REQUEST);
        }
        ActivityChatLink activityChatLink = new ActivityChatLink();
        activityChatLink.setActivity(activity.get());
        activityChatLink.setLink(link);
        activityChatLinkRepository.save(activityChatLink);
        return new ResponseEntity<>("Successfully added link to activity with title " + activity.get().getTitle(), HttpStatus.OK);
    }

    public ResponseEntity<?> getChatLinkByActivityId(Long idOfActivity) {
        Optional<ActivityChatLink> linkByActivityId = activityChatLinkRepository.findByActivity_Id(idOfActivity);
        if (linkByActivityId.isEmpty()) {
            return new ResponseEntity<>("Activity with id " + idOfActivity + " not founded", HttpStatus.OK);
        }
        ActivityChatLink activityChatLink = linkByActivityId.get();
        return new ResponseEntity<>("There`s link for chat of activity " + activityChatLink.getLink(), HttpStatus.OK);
    }

    public ResponseEntity<?> addCommunityLink(String link, Principal principal) {
        if (organisationGroupLinkRepository.existsByOrganisationInfo_AppUser_Username(principal.getName())) {
            return new ResponseEntity<>("Link already exists", HttpStatus.BAD_REQUEST);
        } else if (!organisationService.isUserAreOrganisation(organisationService.findOrganisationByUsername(principal.getName()).get())) {
            return new ResponseEntity<>("Bad id of organisation!", HttpStatus.BAD_REQUEST);
        }
        OrganisationInfo infoAboutOrganisation = organisationService.findAdditionalInfoAboutOrganisation(
                organisationService.findOrganisationByUsername(principal.getName()).get());
        OrganisationGroupLink organisationGroupLink = new OrganisationGroupLink();

        organisationGroupLink.setOrganisationInfo(infoAboutOrganisation);
        organisationGroupLink.setLink(link);
        organisationGroupLinkRepository.save(organisationGroupLink);
        return new ResponseEntity<>("Successfully added community link to organisation " + infoAboutOrganisation.getNameOfOrganisation(), HttpStatus.OK);
    }

    public ResponseEntity<?> getCommunityLinkByOrganisationId(Long idOfOrganisation) {
        Optional<OrganisationGroupLink> organisationGroupLink = organisationGroupLinkRepository.findByOrganisationInfo_AppUser_Id(idOfOrganisation);
        if (organisationGroupLink.isEmpty()) {
            return new ResponseEntity<>("Bad id of organisation!", HttpStatus.BAD_REQUEST);
        }
        OrganisationGroupLink organisationGroupLinkResponse = organisationGroupLink.get();
        return new ResponseEntity<>("There`s link for community of organisation " + organisationGroupLinkResponse.getLink(), HttpStatus.OK);
    }
}

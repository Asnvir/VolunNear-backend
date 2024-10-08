package com.volunnear.services;

import com.volunnear.dtos.requests.FeedbackRequest;
import com.volunnear.dtos.response.FeedbackResponseDTO;
import com.volunnear.dtos.response.OrganisationResponseDTO;
import com.volunnear.entitiy.infos.FeedbackAboutOrganisation;
import com.volunnear.entitiy.infos.OrganisationInfo;
import com.volunnear.entitiy.infos.VolunteerInfo;
import com.volunnear.entitiy.users.AppUser;
import com.volunnear.repositories.infos.FeedbackAboutOrganisationRepository;
import com.volunnear.services.interfaces.FeedbackService;
import com.volunnear.services.interfaces.OrganisationService;
import com.volunnear.services.interfaces.VolunteerService;
import com.volunnear.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final UserService userService;
    private final VolunteerService volunteerService;
    private final OrganisationService organisationService;
    private final FeedbackAboutOrganisationRepository feedbackAboutOrganisationRepository;

    @Override
    public ResponseEntity<String> postFeedbackAboutOrganisation(FeedbackRequest feedbackRequest, Principal principal) {
        if (feedbackRequest.getRate() < 0 || feedbackRequest.getRate() > 5.0) {
            return new ResponseEntity<>("Bad value of rate", HttpStatus.BAD_REQUEST);
        }

        AppUser appUserByUsername = userService.findAppUserByUsername(principal.getName()).get();

        VolunteerInfo volunteerInfo = volunteerService.getVolunteerInfo(appUserByUsername);
        Optional<OrganisationInfo> organisationAndAdditionalInfoById = organisationService.findOrganisationAndAdditionalInfoById(feedbackRequest.getIdOfOrganisation());

        if (organisationAndAdditionalInfoById.isEmpty()) {
            return new ResponseEntity<>("No additional data about selected organisation", HttpStatus.BAD_REQUEST);
        }

        FeedbackAboutOrganisation feedback = new FeedbackAboutOrganisation();
        feedback.setOrganisationInfo(organisationAndAdditionalInfoById.get());
        feedback.setUsernameOfVolunteer(principal.getName());
        feedback.setNameOfVolunteer(volunteerInfo.getRealNameOfUser());
        feedback.setDescription(feedbackRequest.getFeedbackDescription());
        feedback.setRate(feedbackRequest.getRate());
        feedback.setVolunteerInfo(volunteerInfo);
        feedbackAboutOrganisationRepository.save(feedback);
        return new ResponseEntity<>("Feedback successfully added", HttpStatus.OK);
    }
    @Override
    public ResponseEntity<String> updateFeedbackInfoForCurrentOrganisation(UUID idOfFeedback, FeedbackRequest feedbackRequest, Principal principal) {
        Optional<FeedbackAboutOrganisation> feedbackById = feedbackAboutOrganisationRepository.findById(idOfFeedback);
        if (feedbackById.isEmpty() || !principal.getName().equals(feedbackById.get().getUsernameOfVolunteer())) {
            return new ResponseEntity<>("Invalid id of feedback", HttpStatus.BAD_REQUEST);
        }
        FeedbackAboutOrganisation feedbackAboutOrganisation = feedbackById.get();
        feedbackAboutOrganisation.setDescription(feedbackRequest.getFeedbackDescription());
        feedbackAboutOrganisation.setRate(feedbackRequest.getRate());
        feedbackAboutOrganisationRepository.save(feedbackAboutOrganisation);
        return new ResponseEntity<>("Successfully updated feedback", HttpStatus.OK);
    }
    @Override
    public ResponseEntity<String> deleteFeedbackAboutOrganisation(UUID idOfFeedback, Principal principal) {
        Optional<FeedbackAboutOrganisation> feedbackById = feedbackAboutOrganisationRepository.findById(idOfFeedback);
        if (feedbackById.isEmpty() || !principal.getName().equals(feedbackById.get().getUsernameOfVolunteer())) {
            return new ResponseEntity<>("Invalid id of feedback", HttpStatus.BAD_REQUEST);
        }
        feedbackAboutOrganisationRepository.deleteById(idOfFeedback);
        return new ResponseEntity<>("Successfully deleted your feedback", HttpStatus.OK);
    }
    @Override
    public Map<OrganisationResponseDTO, List<FeedbackResponseDTO>> getAllFeedbacksAboutAllOrganisations() {
        List<FeedbackAboutOrganisation> allFeedback = feedbackAboutOrganisationRepository.findAll();
        return getOrganisationResponseDTOMap(allFeedback);
    }
    @Override
    public ResponseEntity<?> getFeedbacksAboutCurrentOrganisation(UUID id) {
        List<FeedbackAboutOrganisation> feedbackAboutOrganisationList =
                feedbackAboutOrganisationRepository.findFeedbackAboutOrganisationByOrganisationInfo_AppUser_Id(id);
        List<FeedbackResponseDTO> feedbackResult = getListOfFeedbacksDTO(feedbackAboutOrganisationList);
        return new ResponseEntity<>(feedbackResult, HttpStatus.OK);
    }

    private Map<OrganisationResponseDTO, List<FeedbackResponseDTO>> getOrganisationResponseDTOMap(List<FeedbackAboutOrganisation> allFeedback) {
        Map<OrganisationInfo, List<FeedbackAboutOrganisation>> feedbackMap =
                allFeedback.stream().collect(Collectors.groupingBy(FeedbackAboutOrganisation::getOrganisationInfo));

        Map<OrganisationResponseDTO, List<FeedbackResponseDTO>> feedbackResult = new HashMap<>();
        for (Map.Entry<OrganisationInfo, List<FeedbackAboutOrganisation>> organisationInfoListEntry : feedbackMap.entrySet()) {
            feedbackResult.put(organisationService.getOrganisationResponseDTO(organisationInfoListEntry.getKey()),
                    getListOfFeedbacksDTO(organisationInfoListEntry.getValue()));
        }
        return feedbackResult;
    }

    private List<FeedbackResponseDTO> getListOfFeedbacksDTO(List<FeedbackAboutOrganisation> feedbackAboutOrganisationList) {
        return feedbackAboutOrganisationList.stream().map(feedbackAboutOrganisation -> {
            String avatarUrl = feedbackAboutOrganisation.getVolunteerInfo() != null
                    ? feedbackAboutOrganisation.getVolunteerInfo().getAvatarUrl()
                    : null;

            return new FeedbackResponseDTO(
                    feedbackAboutOrganisation.getId(),
                    feedbackAboutOrganisation.getRate(),
                    feedbackAboutOrganisation.getDescription(),
                    feedbackAboutOrganisation.getNameOfVolunteer(),
                    feedbackAboutOrganisation.getUsernameOfVolunteer(),
                    avatarUrl
            );
        }).toList();
    }

}

package com.volunnear.services.activities;

import com.volunnear.dtos.response.FeedbackResponseDTO;
import com.volunnear.dtos.response.OrganisationResponseDTO;
import com.volunnear.entitiy.infos.FeedbackAboutOrganisation;
import com.volunnear.entitiy.infos.OrganisationInfo;
import com.volunnear.entitiy.infos.VolunteerInfo;
import com.volunnear.entitiy.users.AppUser;
import com.volunnear.services.users.OrganisationService;
import com.volunnear.services.users.UserService;
import com.volunnear.services.users.VolunteerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import com.volunnear.controllers.FeedbackRequest;
import com.volunnear.repositories.infos.FeedbackAboutOrganisationRepository;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final UserService userService;
    private final VolunteerService volunteerService;
    private final OrganisationService organisationService;
    private final FeedbackAboutOrganisationRepository feedbackAboutOrganisationRepository;

    public ResponseEntity<?> postFeedbackAboutOrganisation(FeedbackRequest feedbackRequest, Principal principal) {
        if (feedbackRequest.getRate() < 0 || feedbackRequest.getRate() > 10){
            return new ResponseEntity<>("Bad value of rate", HttpStatus.BAD_REQUEST);
        }

        AppUser appUserByUsername = userService.findAppUserByUsername(principal.getName()).get();

        VolunteerInfo volunteerInfo = volunteerService.getVolunteerInfo(appUserByUsername);
        Optional<OrganisationInfo> organisationAndAdditionalInfoById = organisationService.findOrganisationAndAdditionalInfoById(feedbackRequest.getIdOfOrganisation());

        if (organisationAndAdditionalInfoById.isEmpty()){
            return new ResponseEntity<>("No additional data about selected organisation", HttpStatus.BAD_REQUEST);
        }

        FeedbackAboutOrganisation feedback = new FeedbackAboutOrganisation();
        feedback.setOrganisationInfo(organisationAndAdditionalInfoById.get());
        feedback.setUsernameOfVolunteer(principal.getName());
        feedback.setNameOfVolunteer(volunteerInfo.getRealNameOfUser());
        feedback.setDescription(feedbackRequest.getFeedbackDescription());
        feedback.setRate(feedbackRequest.getRate());
        feedbackAboutOrganisationRepository.save(feedback);
        return new ResponseEntity<>("Feedback successfully added", HttpStatus.OK);
    }

    public ResponseEntity<?> updateFeedbackInfoForCurrentOrganisation(Long idOfFeedback, FeedbackRequest feedbackRequest, Principal principal) {
        Optional<FeedbackAboutOrganisation> feedbackById = feedbackAboutOrganisationRepository.findById(idOfFeedback);
        if (feedbackById.isEmpty() || !principal.getName().equals(feedbackById.get().getUsernameOfVolunteer())){
            return new ResponseEntity<>("Invalid id of feedback", HttpStatus.BAD_REQUEST);
        }
        FeedbackAboutOrganisation feedbackAboutOrganisation = feedbackById.get();
        feedbackAboutOrganisation.setDescription(feedbackRequest.getFeedbackDescription());
        feedbackAboutOrganisation.setRate(feedbackRequest.getRate());
        feedbackAboutOrganisationRepository.save(feedbackAboutOrganisation);
        return new ResponseEntity<>("Successfully updated feedback", HttpStatus.OK);
    }

    public ResponseEntity<?> deleteFeedbackAboutOrganisation(Long idOfFeedback, Principal principal) {
        Optional<FeedbackAboutOrganisation> feedbackById = feedbackAboutOrganisationRepository.findById(idOfFeedback);
        if (feedbackById.isEmpty() || !principal.getName().equals(feedbackById.get().getUsernameOfVolunteer())){
            return new ResponseEntity<>("Invalid id of feedback", HttpStatus.BAD_REQUEST);
        }
        feedbackAboutOrganisationRepository.deleteById(idOfFeedback);
        return new ResponseEntity<>("Successfully deleted your feedback", HttpStatus.OK);
    }

    public ResponseEntity<?> getAllFeedbacksAboutAllOrganisations() {
        List<FeedbackAboutOrganisation> allFeedback = feedbackAboutOrganisationRepository.findAll();
        Map<OrganisationResponseDTO, List<FeedbackResponseDTO>> feedbackResult = getOrganisationResponseDTOMap(allFeedback);
        return new ResponseEntity<>(feedbackResult, HttpStatus.OK);
    }

    public ResponseEntity<?> getFeedbacksAboutCurrentOrganisation(Long id) {
        List<FeedbackAboutOrganisation> feedbackAboutOrganisationList =
                feedbackAboutOrganisationRepository.findFeedbackAboutOrganisationByOrganisationInfo_AppUser_Id(id);
        if (feedbackAboutOrganisationList.isEmpty()){
            return new ResponseEntity<>("There is no feedback about that organisation", HttpStatus.OK);
        }
        Map<OrganisationResponseDTO, List<FeedbackResponseDTO>> feedbackResult = getOrganisationResponseDTOMap(feedbackAboutOrganisationList);
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
        return feedbackAboutOrganisationList.stream().map(feedbackAboutOrganisation -> new FeedbackResponseDTO(
                feedbackAboutOrganisation.getId(),
                feedbackAboutOrganisation.getRate(),
                feedbackAboutOrganisation.getDescription(),
                feedbackAboutOrganisation.getNameOfVolunteer(),
                feedbackAboutOrganisation.getUsernameOfVolunteer()
                )).toList();
    }

}

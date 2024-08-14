package com.volunnear.services.notifications;

import com.volunnear.dtos.response.OrganisationResponseDTO;
import com.volunnear.entitiy.VolunteerNotificationSubscription;
import com.volunnear.entitiy.users.AppUser;
import com.volunnear.repositories.VolunteerNotificationSubscriptionRepository;
import com.volunnear.services.interfaces.EmailNotificationService;
import com.volunnear.services.interfaces.OrganisationService;
import com.volunnear.services.interfaces.VolunteerService;
import com.volunnear.services.users.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailNotificationServiceImpl implements EmailNotificationService {
    private final UserService userService;
    private final VolunteerService volunteerService;
    private final OrganisationService organisationService;
    private final VolunteerNotificationSubscriptionRepository subscriptionRepository;


    @Override
    @Transactional
    public ResponseEntity<String> subscribeToNotificationByIdOfOrganisation(UUID idOfOrganisation, Principal principal) {
        Optional<AppUser> organisationById = organisationService.findOrganisationById(idOfOrganisation);
        if (organisationById.isEmpty() || !organisationService.isUserAreOrganisation(organisationById.get())) {
            return new ResponseEntity<>("Bad id of organisation", HttpStatus.BAD_REQUEST);
        }
        if (subscriptionRepository.existsByUserVolunteer_UsernameAndUserOrganisation_Id(principal.getName(), idOfOrganisation)) {
            return new ResponseEntity<>("Fail, you are already subscribed", HttpStatus.BAD_REQUEST);
        }
        AppUser appUser = userService.findAppUserByUsername(principal.getName()).get();
        if (!volunteerService.isUserAreVolunteer(appUser)) {
            return new ResponseEntity<>("You are not volunteer", HttpStatus.BAD_REQUEST);
        }
        VolunteerNotificationSubscription subscription = new VolunteerNotificationSubscription();
        subscription.setUserVolunteer(appUser);
        subscription.setUserOrganisation(organisationById.get());
        subscriptionRepository.save(subscription);
        return new ResponseEntity<>("Successfully subscribed to notifications", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> unsubscribeFromNotificationOfOrganisation(UUID idOfOrganisation, Principal principal) {
        Optional<VolunteerNotificationSubscription> subscriptionById = subscriptionRepository.
                findByUserVolunteerUsernameAndUserOrganisationId(principal.getName(), idOfOrganisation);
        if (subscriptionById.isEmpty() || !principal.getName().equals(subscriptionById.get().getUserVolunteer().getUsername())) {
            return new ResponseEntity<>("Bad id of subscription", HttpStatus.BAD_REQUEST);
        }
        subscriptionRepository.delete(subscriptionById.get());
        return new ResponseEntity<>("Successfully unsubscribed from notifications", HttpStatus.OK);
    }
    @Override
    public ResponseEntity<?> getAllSubscriptionsOfVolunteer(Principal principal) {
        List<VolunteerNotificationSubscription> allByUserVolunteerUsername = subscriptionRepository.findAllByUserVolunteer_Username(principal.getName());
        List<OrganisationResponseDTO> organisations = allByUserVolunteerUsername.stream().map(
                subscription -> organisationService.getResponseDTOForSubscriptions(subscription.getUserOrganisation())).collect(Collectors.toList());

        return new ResponseEntity<>(organisations, HttpStatus.OK);
    }

}

package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.services.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class NotificationsController {
    private final EmailNotificationService emailNotificationService;

//    @GetMapping(Routes.SEND_NOTIFICATION)
//    public ResponseEntity<?> sendNotification(){
//        emailNotificationService.sendNotification("","TEST","TEST");
//        return new ResponseEntity<>("Successfully method invoke", HttpStatus.OK);
//    }

    @GetMapping(Routes.GET_ALL_SUBSCRIPTIONS_OF_VOLUNTEER)
    public ResponseEntity<?> getAllSubscriptionsOfVolunteer(Principal principal) {
        return emailNotificationService.getAllSubscriptionsOfVolunteer(principal);
    }

    @PostMapping(Routes.SUBSCRIBE_TO_NOTIFICATIONS_BY_ID_OF_ORGANISATION)
    public ResponseEntity<?> subscribeToNotificationsByIdOfOrganisation(@RequestParam Long idOfOrganisation, Principal principal) {
        return emailNotificationService.subscribeToNotificationByIdOfOrganisation(idOfOrganisation, principal);
    }

    @DeleteMapping(Routes.UNSUBSCRIBE_FROM_NOTIFICATIONS_BY_ID_OF_ORGANISATION)
    public ResponseEntity<?> unsubscribeFromNotificationsByIdOfOrganisations(@RequestParam Long idOfOrganisation, Principal principal) {
        return emailNotificationService.unsubscribeFromNotificationOfOrganisation(idOfOrganisation, principal);
    }
}

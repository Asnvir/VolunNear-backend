package com.volunnear.services.interfaces;

import java.security.Principal;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

public interface EmailNotificationService {

    ResponseEntity<String> subscribeToNotificationByIdOfOrganisation(UUID idOfOrganisation, Principal principal);

    ResponseEntity<String> unsubscribeFromNotificationOfOrganisation(UUID idOfOrganisation, Principal principal);

    ResponseEntity<?> getAllSubscriptionsOfVolunteer(Principal principal);

}

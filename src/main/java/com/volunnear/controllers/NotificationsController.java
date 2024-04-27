package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.response.OrganisationResponseDTO;
import com.volunnear.services.notifications.EmailNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class NotificationsController {
    private final EmailNotificationService emailNotificationService;

    @Operation(summary = "Get all subscriptions of volunteer", description = "Returns List<OrganisationResponseDTO>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List<OrganisationResponseDTO> (list of subscriptions with info about organisations)",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrganisationResponseDTO.class)))),
            @ApiResponse(responseCode = "400", description = "List of subscriptions is empty!")
    })
    @GetMapping(Routes.GET_ALL_SUBSCRIPTIONS_OF_VOLUNTEER)
    public ResponseEntity<?> getAllSubscriptionsOfVolunteer(Principal principal) {
        return emailNotificationService.getAllSubscriptionsOfVolunteer(principal);
    }

    @PostMapping(Routes.SUBSCRIBE_TO_NOTIFICATIONS_BY_ID_OF_ORGANISATION)
    public ResponseEntity<String> subscribeToNotificationsByIdOfOrganisation(@RequestParam UUID idOfOrganisation, Principal principal) {
        return emailNotificationService.subscribeToNotificationByIdOfOrganisation(idOfOrganisation, principal);
    }

    @DeleteMapping(Routes.UNSUBSCRIBE_FROM_NOTIFICATIONS_BY_ID_OF_ORGANISATION)
    public ResponseEntity<String> unsubscribeFromNotificationsByIdOfOrganisations(@RequestParam UUID idOfOrganisation, Principal principal) {
        return emailNotificationService.unsubscribeFromNotificationOfOrganisation(idOfOrganisation, principal);
    }
}

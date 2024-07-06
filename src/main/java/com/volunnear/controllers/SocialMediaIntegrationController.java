package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.services.SocialMediaIntegrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SocialMediaIntegrationController {
    private final SocialMediaIntegrationService socialMediaIntegrationService;

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Add chat link to activity", description = "Requires organisation account (token), link and idOfActivity")
    @PostMapping(Routes.ADD_CHAT_LINK_FOR_ACTIVITY)
    public ResponseEntity<?> addChatLinkToActivity(@RequestParam UUID idOfActivity,
                                                   @RequestBody String link,
                                                   Principal principal) {
        return socialMediaIntegrationService.addChatLinkToActivity(idOfActivity, link, principal);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Add community link", description = "Requires organisation account (token) and link")
    @PostMapping(Routes.ADD_COMMUNITY_LINK)
    public ResponseEntity<?> addCommunityLink(@RequestBody String link,
                                              Principal principal) {
        return socialMediaIntegrationService.addCommunityLink(link, principal);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get chat link by id of activity", description = "Method requires idOfActivity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Link founded"),
            @ApiResponse(responseCode = "400", description = "Bad id of activity!")
    })
    @GetMapping(Routes.GET_CHAT_LINK_BY_ACTIVITY)
    public ResponseEntity<String> getChatLinkByActivity(@RequestParam UUID idOfActivity) {
        return socialMediaIntegrationService.getChatLinkByActivityId(idOfActivity);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get community link by id of organisation", description = "Method requires idOfOrganisation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Link founded"),
            @ApiResponse(responseCode = "400", description = "Bad id of organisation!")
    })
    @GetMapping(Routes.GET_COMMUNITY_LINK_BY_ORGANISATION)
    public ResponseEntity<String> getCommunityLinkByOrganisation(@RequestParam UUID idOfOrganisation) {
        return socialMediaIntegrationService.getCommunityLinkByOrganisationId(idOfOrganisation);
    }
}

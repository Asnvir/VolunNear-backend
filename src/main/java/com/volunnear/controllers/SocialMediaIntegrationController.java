package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.services.SocialMediaIntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class SocialMediaIntegrationController {
    private final SocialMediaIntegrationService socialMediaIntegrationService;

    @PostMapping(Routes.ADD_CHAT_LINK_FOR_ACTIVITY)
    public ResponseEntity<?> addChatLinkToActivity(@RequestParam Long idOfActivity,
                                                   @RequestBody String link,
                                                   Principal principal) {
        return socialMediaIntegrationService.addChatLinkToActivity(idOfActivity, link, principal);
    }

    @PostMapping(Routes.ADD_COMMUNITY_LINK)
    public ResponseEntity<?> addCommunityLink(@RequestBody String link,
                                              Principal principal) {
        return socialMediaIntegrationService.addCommunityLink(link, principal);
    }

    @GetMapping(Routes.GET_CHAT_LINK_BY_ACTIVITY)
    public ResponseEntity<?> getChatLinkByActivity(@RequestParam Long idOfActivity) {
        return socialMediaIntegrationService.getChatLinkByActivityId(idOfActivity);
    }

    @GetMapping(Routes.GET_COMMUNITY_LINK_BY_ORGANISATION)
    public ResponseEntity<?> getCommunityLinkByOrganisation(@RequestParam Long idOfOrganisation) {
        return socialMediaIntegrationService.getCommunityLinkByOrganisationId(idOfOrganisation);
    }
}

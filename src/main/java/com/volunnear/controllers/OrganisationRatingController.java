package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.services.interfaces.OrganisationRatingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;


@RestController
public class OrganisationRatingController {

    @Autowired
    private OrganisationRatingService organisationRatingService;

    /**
     * Endpoint to submit a rating for an organisation.
     * @param id The UUID of the organisation to rate.
     * @param rating The rating value (1 to 5).
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(Routes.RATE_ORGANISATION)
    public void rateOrganisation(@PathVariable UUID id, @RequestParam int rating, Principal principal) {
        organisationRatingService.addOrUpdateRating(id, rating, principal);
    }

    /**
     * Endpoint to retrieve the average rating for an organisation.
     * @param id The UUID of the organisation.
     * @return The average rating as a double.
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(Routes.GET_AVERAGE_RATING)
    public double getAverageRating(@PathVariable UUID id) {
        return organisationRatingService.getAverageRating(id);
    }
}

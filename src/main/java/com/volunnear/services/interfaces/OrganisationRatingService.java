package com.volunnear.services.interfaces;

import java.security.Principal;
import java.util.UUID;

public interface OrganisationRatingService {

    public void addOrUpdateRating(UUID organisationId, int rating, Principal principal);

    public double getAverageRating(UUID organisationId);
}

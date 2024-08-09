package com.volunnear.repositories;

import com.volunnear.entitiy.OrganisationRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrganisationRatingRepository extends JpaRepository<OrganisationRating, UUID> {
    // New method to find by AppUserId through OrganisationInfo
    Optional<OrganisationRating> findByOrganisationAppUserIdAndUserId(UUID appUserId, UUID userId);
}

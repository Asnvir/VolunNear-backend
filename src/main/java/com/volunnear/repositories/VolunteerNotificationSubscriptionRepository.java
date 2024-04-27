package com.volunnear.repositories;

import com.volunnear.entitiy.VolunteerNotificationSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VolunteerNotificationSubscriptionRepository extends JpaRepository<VolunteerNotificationSubscription, UUID> {
    List<VolunteerNotificationSubscription> findAllByUserVolunteer_Username(String username);

    boolean existsByUserVolunteer_UsernameAndUserOrganisation_Id(String username, UUID id);

    Optional<VolunteerNotificationSubscription> findByUserVolunteerUsernameAndUserOrganisationId(String username, UUID organisationId);

    List<VolunteerNotificationSubscription> findAllByUserOrganisationId(UUID id);
}
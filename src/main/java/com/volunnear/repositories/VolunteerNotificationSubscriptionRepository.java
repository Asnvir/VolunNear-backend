package com.volunnear.repositories;

import com.volunnear.entitiy.VolunteerNotificationSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VolunteerNotificationSubscriptionRepository extends JpaRepository<VolunteerNotificationSubscription, Long> {
    List<VolunteerNotificationSubscription> findAllByUserVolunteer_Username(String username);

    boolean existsByUserVolunteer_UsernameAndUserOrganisation_Id(String username, Long id);

    Optional<VolunteerNotificationSubscription> findByUserVolunteerUsernameAndUserOrganisationId(String username, Long organisationId);

    List<VolunteerNotificationSubscription> findAllByUserOrganisationId(Long id);
}
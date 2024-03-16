package com.volunnear.repositories;

import com.volunnear.entitiy.users.AppUser;
import com.volunnear.entitiy.users.VolunteerPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VolunteerPreferenceRepository extends JpaRepository<VolunteerPreference, Long> {
    Optional<VolunteerPreference> findVolunteerPreferenceByVolunteer(AppUser appUser);
}
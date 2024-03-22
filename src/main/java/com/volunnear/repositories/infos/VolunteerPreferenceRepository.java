package com.volunnear.repositories.infos;

import com.volunnear.entitiy.infos.VolunteerPreference;
import com.volunnear.entitiy.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VolunteerPreferenceRepository extends JpaRepository<VolunteerPreference, Long> {
    Optional<VolunteerPreference> findVolunteerPreferenceByVolunteer(AppUser appUser);
}
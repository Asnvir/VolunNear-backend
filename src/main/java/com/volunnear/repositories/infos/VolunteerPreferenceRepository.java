package com.volunnear.repositories.infos;

import com.volunnear.entitiy.infos.VolunteerPreference;
import com.volunnear.entitiy.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VolunteerPreferenceRepository extends JpaRepository<VolunteerPreference, UUID> {
    Optional<VolunteerPreference> findVolunteerPreferenceByVolunteer(AppUser appUser);
}
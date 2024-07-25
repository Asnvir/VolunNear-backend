package com.volunnear.repositories.infos;

import com.volunnear.entitiy.activities.VolunteerInActivity;
import com.volunnear.entitiy.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface VolunteersInActivityRepository extends JpaRepository<VolunteerInActivity, UUID>, JpaSpecificationExecutor<VolunteerInActivity> {
    List<VolunteerInActivity> findAllByUser(AppUser appUser);

    boolean existsByUserAndActivity_Id(AppUser appUser, UUID id);

    void deleteByActivity_IdAndUser_Id(UUID activityId, UUID userId);
}
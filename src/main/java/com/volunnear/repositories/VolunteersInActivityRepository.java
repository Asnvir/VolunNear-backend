package com.volunnear.repositories;

import com.volunnear.entitiy.activities.VolunteerInActivity;
import com.volunnear.entitiy.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VolunteersInActivityRepository extends JpaRepository<VolunteerInActivity, Long> {
    List<VolunteerInActivity> findAllByUser(AppUser appUser);
    boolean existsByUserAndActivity_Id(AppUser appUser, Long id);
    void deleteByActivity_IdAndUser_Id(Long activityId, Long userId);
}
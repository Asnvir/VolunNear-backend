package com.volunnear.repositories;

import com.volunnear.entitiy.activities.Activity;
import com.volunnear.entitiy.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivitiesRepository extends JpaRepository<Activity, Long> {
    List<Activity> findActivitiesByAppUser(AppUser appUser);
}
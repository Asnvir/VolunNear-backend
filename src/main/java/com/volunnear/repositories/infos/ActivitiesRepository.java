package com.volunnear.repositories.infos;

import com.volunnear.entitiy.activities.Activity;
import com.volunnear.entitiy.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActivitiesRepository extends JpaRepository<Activity, UUID>, JpaSpecificationExecutor<Activity> {
    List<Activity> findActivitiesByAppUser(AppUser appUser);

    List<Activity> findActivityByKindOfActivityIgnoreCaseIn(List<String> preferences);

    List<Activity> findActivityByCountryAndCity(String country, String city);

    Optional<Activity> findActivityByAppUserAndId(AppUser appUser, UUID idOfActivity);
}
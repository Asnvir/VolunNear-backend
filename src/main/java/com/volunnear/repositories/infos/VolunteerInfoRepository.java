package com.volunnear.repositories.infos;

import com.volunnear.entitiy.infos.VolunteerInfo;
import com.volunnear.entitiy.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VolunteerInfoRepository extends JpaRepository<VolunteerInfo, UUID> {
    VolunteerInfo getVolunteerInfoByAppUser(AppUser appUser);

    boolean existsByAppUser(AppUser appUser);
}
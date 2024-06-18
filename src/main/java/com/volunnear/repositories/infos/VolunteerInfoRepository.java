package com.volunnear.repositories.infos;

import com.volunnear.entitiy.infos.VolunteerInfo;
import com.volunnear.entitiy.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VolunteerInfoRepository extends JpaRepository<VolunteerInfo, UUID> {
    VolunteerInfo getVolunteerInfoByAppUser(AppUser appUser);

    boolean existsByAppUser(AppUser appUser);
}
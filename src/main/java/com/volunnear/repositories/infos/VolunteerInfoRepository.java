package com.volunnear.repositories.infos;

import com.volunnear.entitiy.infos.VolunteerInfo;
import com.volunnear.entitiy.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerInfoRepository extends JpaRepository<VolunteerInfo, Long> {
    VolunteerInfo getVolunteerInfoByAppUser(AppUser appUser);

    boolean existsByAppUser(AppUser appUser);
}
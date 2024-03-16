package com.volunnear.repositories.users;

import com.volunnear.entitiy.users.AppUser;
import com.volunnear.entitiy.users.VolunteerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerInfoRepository extends JpaRepository<VolunteerInfo, Long> {
    VolunteerInfo getVolunteerInfoByAppUser(AppUser appUser);
}
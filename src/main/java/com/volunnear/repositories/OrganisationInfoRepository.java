package com.volunnear.repositories;

import com.volunnear.entitiy.users.AppUser;
import com.volunnear.entitiy.users.OrganisationInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganisationInfoRepository extends JpaRepository<OrganisationInfo, Long> {
    boolean existsOrganisationInfoByAppUser(AppUser appUser);
}
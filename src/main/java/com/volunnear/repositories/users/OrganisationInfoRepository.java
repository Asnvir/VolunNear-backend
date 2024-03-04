package com.volunnear.repositories.users;

import com.volunnear.entitiy.users.AppUser;
import com.volunnear.entitiy.users.OrganisationInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganisationInfoRepository extends JpaRepository<OrganisationInfo, Long> {
    boolean existsOrganisationInfoByAppUser(AppUser appUser);

    OrganisationInfo findOrganisationInfoByAppUser(AppUser appUser);

    Optional<OrganisationInfo> findOrganisationInfoByNameOfOrganisation(String nameOfOrganisation);
}
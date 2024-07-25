package com.volunnear.repositories.infos;

import com.volunnear.entitiy.activities.Activity;
import com.volunnear.entitiy.infos.OrganisationInfo;
import com.volunnear.entitiy.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface OrganisationInfoRepository extends JpaRepository<OrganisationInfo, UUID>, JpaSpecificationExecutor<OrganisationInfo> {
    OrganisationInfo findOrganisationInfoByAppUser(AppUser appUser);

    Optional<OrganisationInfo> findOrganisationInfoByNameOfOrganisation(String nameOfOrganisation);

    boolean existsByAppUser(AppUser appUser);
}
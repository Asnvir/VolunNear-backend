package com.volunnear.repositories.infos;

import com.volunnear.entitiy.infos.OrganisationInfo;
import com.volunnear.entitiy.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganisationInfoRepository extends JpaRepository<OrganisationInfo, Long> {
    OrganisationInfo findOrganisationInfoByAppUser(AppUser appUser);

    Optional<OrganisationInfo> findOrganisationInfoByNameOfOrganisation(String nameOfOrganisation);

    boolean existsByAppUser(AppUser appUser);
}
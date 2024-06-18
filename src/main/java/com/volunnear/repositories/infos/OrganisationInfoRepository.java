package com.volunnear.repositories.infos;

import com.volunnear.entitiy.infos.OrganisationInfo;
import com.volunnear.entitiy.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganisationInfoRepository extends JpaRepository<OrganisationInfo, UUID> {
    OrganisationInfo findOrganisationInfoByAppUser(AppUser appUser);

    Optional<OrganisationInfo> findOrganisationInfoByNameOfOrganisation(String nameOfOrganisation);

    boolean existsByAppUser(AppUser appUser);
}
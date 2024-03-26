package com.volunnear.repositories.infos;

import com.volunnear.entitiy.infos.OrganisationGroupLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganisationGroupLinkRepository extends JpaRepository<OrganisationGroupLink, Long> {
    Optional<OrganisationGroupLink> findByOrganisationInfo_AppUser_Username(String username);

    Optional<OrganisationGroupLink> findByOrganisationInfo_AppUser_Id(Long id);

    boolean existsByOrganisationInfo_AppUser_Username(String username);
}
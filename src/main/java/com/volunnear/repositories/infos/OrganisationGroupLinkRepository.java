package com.volunnear.repositories.infos;

import com.volunnear.entitiy.infos.OrganisationGroupLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrganisationGroupLinkRepository extends JpaRepository<OrganisationGroupLink, UUID> {
    Optional<OrganisationGroupLink> findByOrganisationInfo_AppUser_Username(String username);

    Optional<OrganisationGroupLink> findByOrganisationInfo_AppUser_Id(UUID id);

    boolean existsByOrganisationInfo_AppUser_Username(String username);
}
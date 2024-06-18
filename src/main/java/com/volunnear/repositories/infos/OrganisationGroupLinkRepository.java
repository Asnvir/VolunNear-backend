package com.volunnear.repositories.infos;

import com.volunnear.entitiy.infos.OrganisationGroupLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganisationGroupLinkRepository extends JpaRepository<OrganisationGroupLink, UUID> {
    Optional<OrganisationGroupLink> findByOrganisationInfo_AppUser_Username(String username);

    Optional<OrganisationGroupLink> findByOrganisationInfo_AppUser_Id(UUID id);

    boolean existsByOrganisationInfo_AppUser_Username(String username);
}
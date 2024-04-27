package com.volunnear.repositories.infos;

import com.volunnear.entitiy.infos.FeedbackAboutOrganisation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FeedbackAboutOrganisationRepository extends JpaRepository<FeedbackAboutOrganisation, UUID> {
    List<FeedbackAboutOrganisation> findFeedbackAboutOrganisationByOrganisationInfo_AppUser_Id(UUID id);
}
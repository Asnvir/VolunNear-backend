package com.volunnear.repositories.infos;

import com.volunnear.entitiy.infos.FeedbackAboutOrganisation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackAboutOrganisationRepository extends JpaRepository<FeedbackAboutOrganisation, Long> {
    List<FeedbackAboutOrganisation> findFeedbackAboutOrganisationByOrganisationInfo_AppUser_Id(Long id);
}
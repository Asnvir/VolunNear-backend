package com.volunnear.services.interfaces;

import com.volunnear.dtos.OrganisationDTO;
import com.volunnear.dtos.enums.SortOrder;
import com.volunnear.dtos.response.OrganisationInfoDTO;
import com.volunnear.dtos.response.OrganisationResponseDTO;
import com.volunnear.entitiy.infos.OrganisationInfo;
import com.volunnear.entitiy.users.AppUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganisationService {

    List<OrganisationResponseDTO> getAllOrganisationsWithInfo(String nameOfOrganisation, String country, String city, SortOrder sortOrder);

    void registerOrganisation(OrganisationDTO organisationDTO);

    OrganisationInfoDTO updateOrganisationInfo(AppUser appUser, OrganisationInfo organisationInfo);

    Optional<AppUser> findOrganisationByUsername(String username);

    Optional<AppUser> findOrganisationById(UUID id);

    Optional<OrganisationInfo> findOrganisationByNameOfOrganisation(String nameOfOrganisation);

    OrganisationInfo findAdditionalInfoAboutOrganisation(AppUser user);

    Optional<OrganisationInfo> findOrganisationAndAdditionalInfoById(UUID idOfOrganisation);

    boolean isUserAreOrganisation(AppUser appUser);

    OrganisationResponseDTO getResponseDTOForSubscriptions(AppUser appUser);

    OrganisationResponseDTO getOrganisationResponseDTO(OrganisationInfo additionalInfoAboutOrganisation);

}

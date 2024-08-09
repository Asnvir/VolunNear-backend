package com.volunnear.services.users;

import com.volunnear.dtos.OrganisationDTO;
import com.volunnear.dtos.enums.SortOrder;
import com.volunnear.dtos.response.OrganisationInfoDTO;
import com.volunnear.dtos.response.OrganisationResponseDTO;
import com.volunnear.entitiy.infos.OrganisationInfo;
import com.volunnear.entitiy.users.AppUser;
import com.volunnear.mappers.OrganisationInfoMapper;
import com.volunnear.repositories.infos.OrganisationInfoRepository;
import com.volunnear.repositories.users.UserRepository;
import com.volunnear.services.interfaces.OrganisationService;
import com.volunnear.specification.OrganisationSpecification;
import com.volunnear.specification.SpecificationEnricher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganisationServiceImpl implements OrganisationService {
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final OrganisationInfoRepository organisationInfoRepository;
    @Autowired
    private final OrganisationInfoMapper organisationInfoMapper;
    private final SpecificationEnricher specificationEnricher;

    @Override
     public List<OrganisationResponseDTO> getAllOrganisationsWithInfo(String nameOfOrganisation, String country, String city, SortOrder sortOrder) {
        Specification<OrganisationInfo> specification = specificationEnricher.createOrganisationInfoSpecification(nameOfOrganisation, country, city, sortOrder);
        List<OrganisationInfo> organisationInfos = organisationInfoRepository.findAll(specification);
        List<OrganisationResponseDTO> organisationResponseDTO = new ArrayList<>();
        for (OrganisationInfo organisationInfo : organisationInfos) {
            organisationResponseDTO.add(getOrganisationResponseDTO(organisationInfo));
        }
        return organisationResponseDTO;
    }
    @Override
    public void registerOrganisation(OrganisationDTO organisationDTO) {
        AppUser organisation = new AppUser();
        organisation.setUsername(organisationDTO.getCredentials().getUsername());
        organisation.setPassword(passwordEncoder.encode(organisationDTO.getCredentials().getPassword()));
        organisation.setRoles(roleService.getRoleByName("ROLE_ORGANISATION"));
        organisation.setEmail(organisationDTO.getCredentials().getEmail());
        addAdditionalDataAboutOrganisation(organisation, organisationDTO);
        userRepository.save(organisation);
    }

    private void addAdditionalDataAboutOrganisation(AppUser organisation, OrganisationDTO organisationDTO) {
        OrganisationInfo organisationInfo = new OrganisationInfo();
        organisationInfo.setAppUser(organisation);
        organisationInfo.setNameOfOrganisation(organisationDTO.getNameOfOrganisation());
        organisationInfo.setCountry(organisationDTO.getCountry());
        organisationInfo.setCity(organisationDTO.getCity());
        organisationInfo.setAddress(organisationDTO.getAddress());
        organisationInfoRepository.save(organisationInfo);
    }
    @Override
    public OrganisationInfoDTO updateOrganisationInfo(AppUser appUser, OrganisationInfo organisationInfo) {
        userRepository.save(appUser);
        organisationInfo.setAppUser(appUser);
        OrganisationInfo updatedOrganizationInfo = organisationInfoRepository.save(organisationInfo);
        return organisationInfoMapper.organisationInfoToOrganisationInfoDTO(updatedOrganizationInfo);
    }
    @Override
    public Optional<AppUser> findOrganisationByUsername(String username) {
        return userRepository.findAppUserByUsername(username);
    }
    @Override
    public Optional<AppUser> findOrganisationById(UUID id) {
        return userRepository.findById(id);
    }
    @Override
    public Optional<OrganisationInfo> findOrganisationByNameOfOrganisation(String nameOfOrganisation) {
        return organisationInfoRepository.findOrganisationInfoByNameOfOrganisation(nameOfOrganisation);
    }
    @Override
    public OrganisationInfo findAdditionalInfoAboutOrganisation(AppUser user) {
        return organisationInfoRepository.findOrganisationInfoByAppUser(user);
    }
    @Override
    public Optional<OrganisationInfo> findOrganisationAndAdditionalInfoById(UUID idOfOrganisation) {
        Optional<AppUser> byId = userRepository.findById(idOfOrganisation);
        return byId.map(organisationInfoRepository::findOrganisationInfoByAppUser);
    }
    @Override
    public boolean isUserAreOrganisation(AppUser appUser) {
        return organisationInfoRepository.existsByAppUser(appUser);
    }
    @Override
    public OrganisationResponseDTO getResponseDTOForSubscriptions(AppUser appUser) {
        return getOrganisationResponseDTO(findAdditionalInfoAboutOrganisation(appUser));
    }
    @Override
    public OrganisationResponseDTO getOrganisationResponseDTO(OrganisationInfo additionalInfoAboutOrganisation) {
        return new OrganisationResponseDTO(
                additionalInfoAboutOrganisation.getAppUser().getId(),
                additionalInfoAboutOrganisation.getNameOfOrganisation(),
                additionalInfoAboutOrganisation.getCountry(),
                additionalInfoAboutOrganisation.getCity(),
                additionalInfoAboutOrganisation.getAddress(),
                additionalInfoAboutOrganisation.getAvatarUrl(),
                additionalInfoAboutOrganisation.getAppUser().getEmail(),
                additionalInfoAboutOrganisation.getAppUser().getUsername(),
                additionalInfoAboutOrganisation.getAverageRating()
        );
    }

}

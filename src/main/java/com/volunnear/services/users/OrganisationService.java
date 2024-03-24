package com.volunnear.services.users;

import com.volunnear.dtos.OrganisationDTO;
import com.volunnear.dtos.response.OrganisationResponseDTO;
import com.volunnear.entitiy.infos.OrganisationInfo;
import com.volunnear.entitiy.users.AppUser;
import com.volunnear.repositories.infos.OrganisationInfoRepository;
import com.volunnear.repositories.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganisationService {
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final OrganisationInfoRepository organisationInfoRepository;

    public ResponseEntity<?> getAllOrganisationsWithInfo() {
        List<OrganisationInfo> organisationInfos = organisationInfoRepository.findAll();
        List<OrganisationResponseDTO> organisationResponseDTO = new ArrayList<>();
        for (OrganisationInfo organisationInfo : organisationInfos) {
            organisationResponseDTO.add(getOrganisationResponseDTO(organisationInfo));
        }
        return new ResponseEntity<>(organisationResponseDTO, HttpStatus.OK);
    }

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

    public void updateOrganisationInfo(AppUser appUser, OrganisationInfo organisationInfo) {
        userRepository.save(appUser);
        organisationInfo.setAppUser(appUser);
        organisationInfoRepository.save(organisationInfo);
    }

    public Optional<AppUser> findOrganisationByUsername(String username) {
        return userRepository.findAppUserByUsername(username);
    }

    public Optional<AppUser> findOrganisationById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<OrganisationInfo> findOrganisationByNameOfOrganisation(String nameOfOrganisation) {
        return organisationInfoRepository.findOrganisationInfoByNameOfOrganisation(nameOfOrganisation);
    }

    public OrganisationInfo findAdditionalInfoAboutOrganisation(AppUser user) {
        return organisationInfoRepository.findOrganisationInfoByAppUser(user);
    }

    public Optional<OrganisationInfo> findOrganisationAndAdditionalInfoById(Long idOfOrganisation) {
        Optional<AppUser> byId = userRepository.findById(idOfOrganisation);
        return byId.map(organisationInfoRepository::findOrganisationInfoByAppUser);
    }

    public boolean isUserAreOrganisation(AppUser appUser) {
        return organisationInfoRepository.existsByAppUser(appUser);
    }

    public OrganisationResponseDTO getResponseDTOForSubscriptions(AppUser appUser) {
        return getOrganisationResponseDTO(findAdditionalInfoAboutOrganisation(appUser));
    }

    public OrganisationResponseDTO getOrganisationResponseDTO(OrganisationInfo additionalInfoAboutOrganisation) {
        return new OrganisationResponseDTO(
                additionalInfoAboutOrganisation.getAppUser().getId(),
                additionalInfoAboutOrganisation.getNameOfOrganisation(),
                additionalInfoAboutOrganisation.getCountry(),
                additionalInfoAboutOrganisation.getCity(),
                additionalInfoAboutOrganisation.getAddress());
    }

}

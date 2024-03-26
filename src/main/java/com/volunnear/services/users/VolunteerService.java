package com.volunnear.services.users;

import com.volunnear.dtos.VolunteerDTO;
import com.volunnear.dtos.requests.PreferencesRequestDTO;
import com.volunnear.dtos.response.VolunteerProfileResponseDTO;
import com.volunnear.entitiy.infos.VolunteerInfo;
import com.volunnear.entitiy.infos.VolunteerPreference;
import com.volunnear.entitiy.users.AppUser;
import com.volunnear.repositories.infos.VolunteerInfoRepository;
import com.volunnear.repositories.infos.VolunteerPreferenceRepository;
import com.volunnear.repositories.users.UserRepository;
import com.volunnear.services.activities.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VolunteerService {
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final ActivityService activityService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final VolunteerInfoRepository volunteerInfoRepository;
    private final VolunteerPreferenceRepository volunteerPreferenceRepository;

    public VolunteerProfileResponseDTO getVolunteerProfile(Principal principal) {
        AppUser appUser = loadUserFromDbByUsername(principal);
        VolunteerInfo volunteerInfo = volunteerInfoRepository.getVolunteerInfoByAppUser(appUser);
        Optional<VolunteerPreference> volunteerPreferenceByVolunteer = volunteerPreferenceRepository.findVolunteerPreferenceByVolunteer(appUser);
        VolunteerProfileResponseDTO profileResponse = new VolunteerProfileResponseDTO();

        profileResponse.setEmail(appUser.getEmail());
        profileResponse.setUsername(principal.getName());
        profileResponse.setRealName(volunteerInfo.getRealNameOfUser());
        if (volunteerPreferenceByVolunteer.isEmpty()) {
            profileResponse.setPreferences(List.of("Preferences is empty"));
        } else profileResponse.setPreferences(volunteerPreferenceByVolunteer.get().getPreferences());
        profileResponse.setActivitiesDTO(activityService.getActivitiesOfVolunteer(appUser));

        return profileResponse;
    }

    public String setPreferencesForVolunteer(PreferencesRequestDTO preferencesRequestDTO, Principal principal) {
        AppUser appUser = loadUserFromDbByUsername(principal);
        VolunteerPreference preference = new VolunteerPreference();
        preference.addPreferences(preferencesRequestDTO.getPreferences());
        preference.setVolunteer(appUser);
        volunteerPreferenceRepository.save(preference);
        return "Successfully set your preferences";
    }

    public Optional<VolunteerPreference> getPreferencesOfUser(Principal principal) {
        AppUser appUser = loadUserFromDbByUsername(principal);
        return volunteerPreferenceRepository.findVolunteerPreferenceByVolunteer(appUser);
    }

    public VolunteerInfo getVolunteerInfo(AppUser appUser) {
        return volunteerInfoRepository.getVolunteerInfoByAppUser(appUser);
    }

    public void updateVolunteerInfo(AppUser appUser, VolunteerInfo volunteerInfo) {
        userRepository.save(appUser);
        volunteerInfoRepository.save(volunteerInfo);
    }

    public void registerVolunteer(VolunteerDTO volunteerDTO) {
        AppUser appUser = new AppUser();
        appUser.setUsername(volunteerDTO.getCredentials().getUsername());
        appUser.setPassword(passwordEncoder.encode(volunteerDTO.getCredentials().getPassword()));
        appUser.setEmail(volunteerDTO.getCredentials().getEmail());
        appUser.setRoles(roleService.getRoleByName("ROLE_VOLUNTEER"));
        userRepository.save(appUser);
        addAdditionalInfo(appUser, volunteerDTO.getNameOfUser());
    }

    public boolean isUserAreVolunteer(AppUser appUser) {
        return volunteerInfoRepository.existsByAppUser(appUser);
    }

    private void addAdditionalInfo(AppUser appUser, String realName) {
        VolunteerInfo volunteerInfo = new VolunteerInfo();
        volunteerInfo.setRealNameOfUser(realName);
        volunteerInfo.setAppUser(appUser);
        volunteerInfoRepository.save(volunteerInfo);
    }

    private AppUser loadUserFromDbByUsername(Principal principal) {
        return userRepository.findAppUserByUsername(principal.getName()).get();
    }
}

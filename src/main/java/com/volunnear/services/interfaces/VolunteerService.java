package com.volunnear.services.interfaces;

import com.volunnear.dtos.VolunteerDTO;
import com.volunnear.dtos.requests.PreferencesRequestDTO;
import com.volunnear.dtos.response.VolunteerInfoDTO;
import com.volunnear.dtos.response.VolunteerProfileResponseDTO;
import com.volunnear.entitiy.infos.VolunteerInfo;
import com.volunnear.entitiy.infos.VolunteerPreference;
import com.volunnear.entitiy.users.AppUser;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

public interface VolunteerService {

    VolunteerProfileResponseDTO getVolunteerProfile(Principal principal);

    String setPreferencesForVolunteer(PreferencesRequestDTO preferencesRequestDTO, Principal principal);

    Optional<VolunteerPreference> getPreferencesOfUser(Principal principal);

    VolunteerInfo getVolunteerInfo(AppUser appUser);

    VolunteerInfoDTO updateVolunteerInfo(AppUser appUser, VolunteerInfo volunteerInfo);

    void registerVolunteer(VolunteerDTO volunteerDTO);

    boolean isUserAreVolunteer(AppUser appUser);

    boolean isMyActivity(Principal principal, UUID idOfActivity);


}

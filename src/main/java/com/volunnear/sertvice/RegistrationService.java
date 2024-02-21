package com.volunnear.sertvice;

import com.volunnear.entitiy.AppUser;
import com.volunnear.entitiy.TVolunteer;
import com.volunnear.model.Volunteer;
import com.volunnear.repository.UserRepository;
import com.volunnear.repository.VolunteerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    private final UserRepository userRepository;
    private final VolunteerRepository volunteerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public RegistrationService(UserRepository userRepository, VolunteerRepository volunteerRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.volunteerRepository = volunteerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerVolunteer(Volunteer volunteer) {
        AppUser appUser = new AppUser();
        appUser.setUsername(volunteer.getCredentials().getLogin());
        appUser.setPassword(passwordEncoder.encode(volunteer.getCredentials().getPassword()));
        appUser.setAuthority("ROLE_VOLUNTEER");
        userRepository.save(appUser);
        addDataAboutVolunteer(volunteer, appUser);
    }

    private void addDataAboutVolunteer(Volunteer volunteer, AppUser appUser) {
        TVolunteer tVolunteer = new TVolunteer();
        tVolunteer.setAppUser(appUser);
        tVolunteer.setRealName(volunteer.getNameOfUser());
        volunteerRepository.save(tVolunteer);
    }
}

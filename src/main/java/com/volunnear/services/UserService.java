package com.volunnear.services;

import com.volunnear.dtos.VolunteerDTO;
import com.volunnear.entitiy.AppUserVolunteer;
import com.volunnear.repositories.RoleRepository;
import com.volunnear.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUserVolunteer appUserVolunteer = userRepository.findAppUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User with " + username + " not found")
        ));
        return new User(
                appUserVolunteer.getUsername(),
                appUserVolunteer.getPassword(),
                appUserVolunteer.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList()
        );
    }

    public void registerVolunteer(VolunteerDTO volunteerDTO) {
        if (userRepository.existsAppUserByUsername(volunteerDTO.getNameOfUser())) {
            throw new AuthenticationServiceException("User with that username already exists");
        }
        AppUserVolunteer appUserVolunteer = new AppUserVolunteer();
        appUserVolunteer.setUsername(volunteerDTO.getCredentials().getUsername());
        appUserVolunteer.setPassword(passwordEncoder.encode(volunteerDTO.getCredentials().getPassword()));
        appUserVolunteer.setRoles(List.of(roleRepository.findRoleByName("ROLE_VOLUNTEER")));
        appUserVolunteer.setRealName(volunteerDTO.getNameOfUser());
        userRepository.save(appUserVolunteer);
    }
}

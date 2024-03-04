package com.volunnear.services.users;

import com.volunnear.dtos.VolunteerDTO;
import com.volunnear.entitiy.users.AppUser;
import com.volunnear.repositories.users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findAppUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User with " + username + " not found")
        ));
        return new User(
                appUser.getUsername(),
                appUser.getPassword(),
                appUser.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList()
        );
    }

    public void registerVolunteer(VolunteerDTO volunteerDTO) {
        AppUser appUser = new AppUser();
        appUser.setUsername(volunteerDTO.getCredentials().getUsername());
        appUser.setPassword(passwordEncoder.encode(volunteerDTO.getCredentials().getPassword()));
        appUser.setEmail(volunteerDTO.getCredentials().getEmail());
        appUser.setRoles(roleService.getRoleByName("ROLE_VOLUNTEER"));
        userRepository.save(appUser);
    }

    public Optional<AppUser> findAppUserByUsername(String username) {
        return userRepository.findAppUserByUsername(username);
    }
}

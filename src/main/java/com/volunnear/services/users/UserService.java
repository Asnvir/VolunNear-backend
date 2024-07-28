package com.volunnear.services.users;

import com.volunnear.entitiy.users.AppUser;
import com.volunnear.dtos.CustomUserDetails;
import com.volunnear.repositories.users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findAppUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User with " + username + " not found")
        ));
        return CustomUserDetails.builder()
                .username(appUser.getUsername())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .password(appUser.getPassword())
                .authorities(appUser.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet()))
                .id(appUser.getId())
                .build();
    }

    public Optional<AppUser> findAppUserByUsername(String username) {
        return userRepository.findAppUserByUsername(username);
    }

    public void updatePassword(AppUser user, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    public boolean checkIfOldPasswordMatches(AppUser user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }
}

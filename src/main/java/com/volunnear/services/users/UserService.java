package com.volunnear.services.users;

import com.volunnear.entitiy.users.AppUser;
import com.volunnear.dtos.CustomUserDetails;
import com.volunnear.exceptions.NotFoundException;
import com.volunnear.repositories.users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;


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

    public AppUser findAppUserByUsername(String username) {
        return userRepository.findAppUserByUsername(username).orElseThrow(
                () -> new NotFoundException("User with username " + username + " not found")
        );
    }
}

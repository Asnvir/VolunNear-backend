package com.volunnear.repositories.users;

import com.volunnear.entitiy.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findAppUserByUsername(String username);

    boolean existsAppUserByUsername(String username);
}

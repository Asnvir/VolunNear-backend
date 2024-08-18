package com.volunnear.repositories.users;

import com.volunnear.entitiy.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findAppUserByUsername(String username);

    Optional<AppUser> findAppUserByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE AppUser u SET u.password = :password WHERE u.email = :email")
    void updatePasswordByEmail(@Param("password") String password, @Param("email") String email);

}

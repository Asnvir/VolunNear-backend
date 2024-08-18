package com.volunnear.repositories;

import com.volunnear.entitiy.ForgotPassword;
import com.volunnear.entitiy.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {
    Optional<ForgotPassword> findByOtpAndAppUser(Integer otp, AppUser appUser);

    Optional<ForgotPassword> findByAppUserId(UUID userID);
}

package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.MailBody;
import com.volunnear.entitiy.ForgotPassword;
import com.volunnear.entitiy.users.AppUser;
import com.volunnear.repositories.ForgotPasswordRepository;
import com.volunnear.repositories.users.UserRepository;
import com.volunnear.services.EmailService;
import com.volunnear.utils.ChangePassword;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Date;
import java.util.Random;

import static com.volunnear.Constants.EXPIRATION_TIME_5_MIN;

@RestController
public class ForgotPasswordController {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    public ForgotPasswordController(UserRepository userRepository, EmailService emailService, ForgotPasswordRepository forgotPasswordRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = Routes.VERIFY_EMAIL + "/{email}")
    @Transactional
    public ResponseEntity<String> verifyEmail(@PathVariable String email) {
        AppUser appUser = userRepository.findAppUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));

        Integer otp = otpGenerator();

        ForgotPassword forgotPassword = ForgotPassword.builder()
                .otp(otp)
                .expiryDate(new Date(System.currentTimeMillis() + EXPIRATION_TIME_5_MIN))
                .appUser(appUser)
                .build();

        ForgotPassword savedForgotPassword = forgotPasswordRepository.save(forgotPassword);

        if (savedForgotPassword.getFpID() == null) {
            throw new RuntimeException("Failed to save OTP");
        }

        MailBody mailBody = MailBody.builder()
                .to(email)
                .subject("OTP for forgot password request")
                .text("This is the OTP to reset your password: " + otp)
                .build();

        emailService.sendSimpleMessage(mailBody);

        return ResponseEntity.ok("OTP sent to email");
    }

    @PostMapping(value = Routes.VERIFY_OTP + "/{otp}/{email}")
    public ResponseEntity<String> verifyOTP(@PathVariable Integer otp, @PathVariable String email) {
        AppUser appUser = userRepository.findAppUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));

        ForgotPassword forgotPassword = forgotPasswordRepository.findByOtpAndAppUser(otp, appUser)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (forgotPassword.getExpiryDate().before(Date.from(Instant.now()))) {
            forgotPasswordRepository.delete(forgotPassword);
            return ResponseEntity.badRequest().body("OTP expired");
        }

        return ResponseEntity.ok("OTP verified");
    }

    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 900_000);
    }

    @PostMapping(value = Routes.CHANGE_PASSWORD + "/{email}")
    public ResponseEntity<String> changePassword(@PathVariable String email,
                                                 @RequestBody ChangePassword changePassword) {
        if (!changePassword.arePasswordsEqual()) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }

        String encodedPassword = passwordEncoder.encode(changePassword.getNewPassword());

        userRepository.updatePasswordByEmail(encodedPassword, email);

        AppUser updatedUser = userRepository.findAppUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " not found after update"));

        if (passwordEncoder.matches(changePassword.getNewPassword(), updatedUser.getPassword())) {
            return ResponseEntity.ok("Password changed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update password");
        }
    }
}

package com.volunnear.services;


import com.volunnear.dtos.forgotPassword.MailBody;
import com.volunnear.dtos.forgotPassword.ResponseForgotPasswordDTO;
import com.volunnear.entitiy.ForgotPassword;
import com.volunnear.entitiy.users.AppUser;
import com.volunnear.repositories.ForgotPasswordRepository;
import com.volunnear.repositories.users.UserRepository;
import com.volunnear.services.interfaces.EventMailSenderService;
import com.volunnear.services.interfaces.ForgotPasswordService;
import com.volunnear.utils.ChangePassword;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Random;

import static com.volunnear.Constants.EXPIRATION_TIME_5_MIN;

@Service
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final UserRepository userRepository;
    private final ForgotPasswordRepository forgotPasswordRepository;
//    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EventMailSenderService eventMailSenderService;


    @Override
    public ResponseForgotPasswordDTO verifyEmail(String email) {
        try {
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

            eventMailSenderService.sendOtpEmail(email, otp);
            return new ResponseForgotPasswordDTO(true, "OTP sent to email");
        } catch (Exception e) {
            return new ResponseForgotPasswordDTO(false, e.getMessage());
        }
    }

    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 900_000);
    }

    @Override
    public ResponseForgotPasswordDTO verifyOTP(String email, Integer otp) {
        try {
            AppUser appUser = userRepository.findAppUserByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));

            ForgotPassword forgotPassword = forgotPasswordRepository.findByOtpAndAppUser(otp, appUser)
                    .orElseThrow(() -> new RuntimeException("OTP not found"));

            if (forgotPassword.getExpiryDate().before(Date.from(Instant.now()))) {
                forgotPasswordRepository.delete(forgotPassword);
                return new ResponseForgotPasswordDTO(false, "OTP expired");
            }

            return new ResponseForgotPasswordDTO(true, "OTP verified");
        } catch (Exception e) {
            return new ResponseForgotPasswordDTO(false, e.getMessage());
        }
    }

    @Override
    public ResponseForgotPasswordDTO changePassword(String email, ChangePassword changePassword) {
        try {
            AppUser appUser = userRepository.findAppUserByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));

            ForgotPassword forgotPassword = forgotPasswordRepository.findByAppUserId(appUser.getId())
                    .orElseThrow(() -> new RuntimeException("ForgotPassword entry not found for email " + email));

            if (!changePassword.arePasswordsEqual()) {
                forgotPasswordRepository.delete(forgotPassword);
                return new ResponseForgotPasswordDTO(false, "Passwords do not match");
            }

            String encodedPassword = passwordEncoder.encode(changePassword.getNewPassword());

            userRepository.updatePasswordByEmail(encodedPassword, email);

            AppUser updatedUser = userRepository.findAppUserByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User with email " + email + " not found after update"));

            if (passwordEncoder.matches(changePassword.getNewPassword(), updatedUser.getPassword())) {
                forgotPasswordRepository.delete(forgotPassword);
                return new ResponseForgotPasswordDTO(true, "Password changed successfully");
            } else {
                forgotPasswordRepository.delete(forgotPassword);
                return new ResponseForgotPasswordDTO(false, "Password update failed");
            }
        } catch (Exception e) {

            AppUser appUser = userRepository.findAppUserByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));

            ForgotPassword forgotPassword = forgotPasswordRepository.findByAppUserId(appUser.getId())
                    .orElseThrow(() -> new RuntimeException("ForgotPassword entry not found for email " + email));

            forgotPasswordRepository.delete(forgotPassword);

            return new ResponseForgotPasswordDTO(false, e.getMessage());
        }
    }

}

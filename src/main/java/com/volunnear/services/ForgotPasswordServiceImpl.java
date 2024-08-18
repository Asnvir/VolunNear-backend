package com.volunnear.services;


import com.volunnear.dtos.forgotPassword.MailBody;
import com.volunnear.dtos.forgotPassword.ResponseForgotPasswordDTO;
import com.volunnear.entitiy.ForgotPassword;
import com.volunnear.entitiy.users.AppUser;
import com.volunnear.repositories.ForgotPasswordRepository;
import com.volunnear.repositories.users.UserRepository;
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
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;


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

            MailBody mailBody = MailBody.builder()
                    .to(email)
                    .subject("OTP for forgot password request")
                    .text("This is the OTP to reset your password: " + otp)
                    .build();

            emailService.sendSimpleMessage(mailBody);
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
    public ResponseForgotPasswordDTO changePassword(String email, String newPassword, String repeatedNewPassword) {
        try {
            // Create ChangePassword object to validate the passwords
            ChangePassword changePassword = new ChangePassword(newPassword, repeatedNewPassword);

            // Find the AppUser by email
            AppUser appUser = userRepository.findAppUserByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));

            // Find the ForgotPassword entry associated with the AppUser
            ForgotPassword forgotPassword = forgotPasswordRepository.findByAppUser(appUser)
                    .orElseThrow(() -> new RuntimeException("ForgotPassword entry not found for email " + email));

            // Check if passwords match
            if (!changePassword.arePasswordsEqual()) {
                // Delete the ForgotPassword entry even if passwords do not match
                forgotPasswordRepository.delete(forgotPassword);
                return new ResponseForgotPasswordDTO(false, "Passwords do not match");
            }

            // Encode the new password
            String encodedPassword = passwordEncoder.encode(changePassword.getNewPassword());

            // Update the password in the database
            userRepository.updatePasswordByEmail(encodedPassword, email);

            // Fetch the user again to verify the password change
            AppUser updatedUser = userRepository.findAppUserByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User with email " + email + " not found after update"));

            // Verify if the new password is correctly set
            if (passwordEncoder.matches(changePassword.getNewPassword(), updatedUser.getPassword())) {
                // Delete the ForgotPassword entry if password change was successful
                forgotPasswordRepository.delete(forgotPassword);
                return new ResponseForgotPasswordDTO(true, "Password changed successfully");
            } else {
                // Delete the ForgotPassword entry even if password update failed
                forgotPasswordRepository.delete(forgotPassword);
                return new ResponseForgotPasswordDTO(false, "Password update failed");
            }
        } catch (Exception e) {
            // Ensure ForgotPassword entry is deleted in case of an exception
            forgotPasswordRepository.findByAppUser(userRepository.findAppUserByEmail(email)
                            .orElseThrow(() -> new RuntimeException("User with email " + email + " not found")))
                    .ifPresent(forgotPasswordRepository::delete);
            return new ResponseForgotPasswordDTO(false, e.getMessage());
        }
    }

}

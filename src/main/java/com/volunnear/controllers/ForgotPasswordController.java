package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.forgotPassword.ResponseForgotPasswordDTO;
import com.volunnear.services.interfaces.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    @PostMapping(value = Routes.VERIFY_EMAIL + "/{email}")
    public ResponseEntity<ResponseForgotPasswordDTO> verifyEmail(@PathVariable String email) {
        ResponseForgotPasswordDTO response = forgotPasswordService.verifyEmail(email);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping(value = Routes.VERIFY_OTP + "/{otp}/{email}")
    public ResponseEntity<ResponseForgotPasswordDTO> verifyOTP(@PathVariable Integer otp, @PathVariable String email) {
        ResponseForgotPasswordDTO response = forgotPasswordService.verifyOTP(email, otp);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping(value = Routes.CHANGE_PASSWORD + "/{email}/{newPassword}/{repeatedNewPassword}")
    public ResponseEntity<ResponseForgotPasswordDTO> changePassword(
            @PathVariable String email,
            @PathVariable String newPassword,
            @PathVariable String repeatedNewPassword) {

        // Call the service to change the password
        ResponseForgotPasswordDTO response = forgotPasswordService.changePassword(email, newPassword, repeatedNewPassword);

        // Return the appropriate response based on the service result
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}

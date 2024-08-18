package com.volunnear.services.interfaces;

import com.volunnear.dtos.forgotPassword.ResponseForgotPasswordDTO;

public interface ForgotPasswordService {
    ResponseForgotPasswordDTO verifyEmail(String email);
    ResponseForgotPasswordDTO verifyOTP(String email, Integer otp);
    ResponseForgotPasswordDTO changePassword(String email, String newPassword, String repeatedNewPassword);
}

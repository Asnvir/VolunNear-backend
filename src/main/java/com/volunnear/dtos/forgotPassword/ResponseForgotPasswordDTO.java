package com.volunnear.dtos.forgotPassword;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseForgotPasswordDTO {
    private final boolean success;
    private final String message;
}

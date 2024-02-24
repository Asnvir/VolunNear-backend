package com.volunnear.exceptions;

import lombok.Data;

import java.util.Date;

@Data
public class AuthErrorException {
    private int status;
    private String message;
    private Date timestamp;

    public AuthErrorException(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}

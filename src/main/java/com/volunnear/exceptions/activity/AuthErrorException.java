package com.volunnear.exceptions.activity;

public class AuthErrorException extends RuntimeException{
    public AuthErrorException(String message) {
        super(message);
    }
}

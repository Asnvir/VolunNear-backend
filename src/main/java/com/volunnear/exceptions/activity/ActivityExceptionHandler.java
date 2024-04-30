package com.volunnear.exceptions.activity;

import com.volunnear.controllers.ActivityController;
import com.volunnear.exceptions.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {ActivityController.class})
public class ActivityExceptionHandler {

    @ExceptionHandler(AuthErrorException.class)
    public ResponseEntity<ApiError> handleAuthErrorException(AuthErrorException ex) {
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ActivityNotFoundException.class)
    public ResponseEntity<ApiError> handleActivityNotFoundException(ActivityNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
}

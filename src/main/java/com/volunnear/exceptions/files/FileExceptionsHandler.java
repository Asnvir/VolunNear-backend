package com.volunnear.exceptions.files;

import com.volunnear.controllers.FileUploadController;
import com.volunnear.exceptions.ApiError;
import com.volunnear.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {FileUploadController.class})
public class FileExceptionsHandler {

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException e) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

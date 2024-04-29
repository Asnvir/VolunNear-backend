package com.volunnear.exceptions;

import org.springframework.http.HttpStatus;


public record ApiError(HttpStatus statusCode, String message) {}


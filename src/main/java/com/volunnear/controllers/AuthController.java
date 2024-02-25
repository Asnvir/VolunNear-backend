package com.volunnear.controllers;

import com.volunnear.dtos.RegistrationRequestDTO;
import com.volunnear.dtos.jwt.JwtRequest;
import com.volunnear.services.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/api/login")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @PostMapping(value = "/api/registration/volunteer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registrationOfVolunteer(@RequestBody RegistrationRequestDTO registrationRequestDto) {
        return authService.registrationOfVolunteer(registrationRequestDto);
    }
}

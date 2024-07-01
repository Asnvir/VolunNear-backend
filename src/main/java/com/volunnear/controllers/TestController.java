package com.volunnear.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//TODO: only for tests, delete in production
@RestController
@RequestMapping("/test")
@SecurityRequirement(name = "Keycloak")
public class TestController {

    @GetMapping("/datasource-url")
    public ResponseEntity<String> getDatasourceUrl() {
        return ResponseEntity.ok("Datasource URL: " + System.getenv("DB_URL"));
    }

    @GetMapping("/check-status")
    public ResponseEntity<String> checkStatus() {
        return ResponseEntity.ok("Status OK");
    }
}
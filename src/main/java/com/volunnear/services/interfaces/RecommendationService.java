package com.volunnear.services.interfaces;

import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface RecommendationService {

     ResponseEntity<?> generateRecommendations(Principal principal);
}

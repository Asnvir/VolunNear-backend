package com.volunnear.services.interfaces;

import com.volunnear.dtos.geoLocation.LocationDTO;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface RecommendationService {

     ResponseEntity<?> generateRecommendations(LocationDTO locationDTO, Principal principal);
}

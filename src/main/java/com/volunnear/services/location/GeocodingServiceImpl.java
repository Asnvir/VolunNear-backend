package com.volunnear.services.location;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volunnear.dtos.geoLocation.Location;
import com.volunnear.services.interfaces.GeocodingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@Slf4j
public class GeocodingServiceImpl implements GeocodingService {

    private static final String GEOCODING_API_URL = "https://geocode.maps.co/search";

    @Value("${GOOGLE_MAPS_API_KEY}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Location getCoordinates(String address) {
        try {
            String urlString = String.format("%s?q=%s&api_key=%s", GEOCODING_API_URL, address, apiKey);
            String response = restTemplate.getForObject(urlString, String.class);
            return parseLocation(response);
        } catch (Exception e) {
            log.error("Error getting location for address: {}", address, e);
            return new Location("0", "0");
        }
    }

    private Location parseLocation(String jsonString) throws Exception {
        JsonNode root = objectMapper.readTree(jsonString);
        JsonNode firstResult = root.path(0);
        String lat = firstResult.path("lat").asText();
        String lon = firstResult.path("lon").asText();
        return new Location(lat, lon);
    }
}

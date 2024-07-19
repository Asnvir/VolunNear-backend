package com.volunnear.services.interfaces;

import com.volunnear.dtos.geoLocation.LocationDTO;

public interface GeocodingService {

    public LocationDTO getCoordinates(String city, String street, String houseNumber);
}

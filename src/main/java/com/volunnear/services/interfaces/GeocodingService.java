package com.volunnear.services.interfaces;

import com.volunnear.dtos.geoLocation.Location;

public interface GeocodingService {

    public Location getCoordinates(String address);
}

package com.volunnear.dtos.geoLocation;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {
    public double latitude;
    public double longitude;
}

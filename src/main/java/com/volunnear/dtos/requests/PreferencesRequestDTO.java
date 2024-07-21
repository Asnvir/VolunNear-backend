package com.volunnear.dtos.requests;

import com.volunnear.dtos.enums.ActivityType;
import com.volunnear.dtos.geoLocation.LocationDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreferencesRequestDTO {
    private List<ActivityType> preferences;
}

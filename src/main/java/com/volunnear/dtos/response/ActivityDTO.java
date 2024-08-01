package com.volunnear.dtos.response;

import com.volunnear.dtos.enums.ActivityType;
import com.volunnear.dtos.geoLocation.LocationDTO;
import com.volunnear.entitiy.GalleryImage;
import com.volunnear.entitiy.activities.Activity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO {
    private UUID id;
    private String title;
    private String description;
    private String country;
    private String city;
    private ActivityType kindOfActivity;
    private Date dateOfPlace;
    private LocationDTO locationDTO;
    private Double distance;
    private String coverImage;
    private List<String> galleryImages;
}

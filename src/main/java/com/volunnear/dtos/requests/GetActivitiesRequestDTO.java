package com.volunnear.dtos.requests;

import com.volunnear.dtos.enums.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class GetActivitiesRequestDTO{
    private String title;
    private String description;
    private String country;
    private String city;
    private ActivityType kindOfActivity;
    private Date dateOfPlace;
    private boolean ascending;
}

package com.volunnear.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO {
    private Long id;
    private String city;
    private String country;
    private Date dateOfPlace;
    private String description;
    private String title;
    private String kindOfActivity;
}

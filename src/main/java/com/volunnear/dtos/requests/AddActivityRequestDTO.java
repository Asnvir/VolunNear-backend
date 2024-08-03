package com.volunnear.dtos.requests;

import com.volunnear.dtos.enums.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddActivityRequestDTO {
    private String title;
    private String description;
    private String country;
    private String city;
    private String street;
    private String houseNumber;
    private ActivityType kindOfActivity;
    private String dateOfPlace;
}

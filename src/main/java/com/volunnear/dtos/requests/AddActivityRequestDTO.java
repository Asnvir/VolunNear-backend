package com.volunnear.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddActivityRequestDTO {
    private String title;
    private String description;
    private String country;
    private String city;
    private String kindOfActivity;
}

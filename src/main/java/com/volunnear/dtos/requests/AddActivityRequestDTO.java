package com.volunnear.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class AddActivityRequestDTO {

    @NotBlank(message = "Title is mandatory and cannot be blank")
    private String title;


    @NotBlank(message = "Description is mandatory and cannot be blank")
    private String description;

    @NotBlank(message = "Country is mandatory and cannot be blank")
    private String country;


    @NotBlank(message = "City is mandatory and cannot be blank")
    private String city;


    @NotBlank(message = "Kind of activity is mandatory and cannot be blank")
    private String kindOfActivity;
}

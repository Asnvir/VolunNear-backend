package com.volunnear.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class RegistrationOrganisationRequestDTO {
    @NotBlank(message = "Username is mandatory and cannot be blank")
    private String username;
    @NotBlank(message = "Password is mandatory and cannot be blank")
    private String password;
    @NotBlank(message = "Email is mandatory and cannot be blank")
    private String email;
    @NotBlank(message = "Name of organisation is mandatory and cannot be blank")
    private String nameOfOrganisation;
    @NotBlank(message = "Country is mandatory and cannot be blank")
    private String country;
    @NotBlank(message = "City is mandatory and cannot be blank")
    private String city;
    @NotBlank(message = "Address is mandatory and cannot be blank")
    private String address;
}

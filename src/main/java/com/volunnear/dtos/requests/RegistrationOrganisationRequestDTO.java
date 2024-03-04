package com.volunnear.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationOrganisationRequestDTO {
    private String username;
    private String password;
    private String email;
    private String nameOfOrganisation;
    private String country;
    private String city;
    private String address;
    private String industry;
}

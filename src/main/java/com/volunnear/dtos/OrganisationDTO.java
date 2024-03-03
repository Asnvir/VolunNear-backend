package com.volunnear.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganisationDTO {
    private Credentials credentials;
    private String nameOfOrganisation;
    private String country;
    private String city;
    private String address;
    private String industry;
}

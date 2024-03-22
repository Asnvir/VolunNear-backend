package com.volunnear.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganisationResponseDTO {
    private Long id;
    private String nameOfOrganisation;
    private String country;
    private String city;
    private String address;
}

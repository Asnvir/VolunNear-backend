package com.volunnear.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrganisationInfoRequestDTO {
    private String email;
    private String username;
    private String nameOfOrganisation;
    private String country;
    private String city;
    private String address;
}

package com.volunnear.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganisationInfoDTO {

    private UUID id;

    private String email;

    private String userName;

    private String nameOfOrganisation;

    private String country;

    private String city;

    private String address;
}

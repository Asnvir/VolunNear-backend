package com.volunnear.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganisationResponseDTO {
    private UUID id;
    private String nameOfOrganisation;
    private String country;
    private String city;
    private String address;
    private String avatarUrl;
    private String email;
    private String username;
    private double rating;
}

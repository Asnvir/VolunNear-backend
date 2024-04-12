package com.volunnear.dtos;

import com.volunnear.dtos.response.ActivityDTO;
import com.volunnear.dtos.response.OrganisationResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityNotificationDTO {
    private ActivityDTO activityDTO;
    private OrganisationResponseDTO organisationResponseDTO;
}

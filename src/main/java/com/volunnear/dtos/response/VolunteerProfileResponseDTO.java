package com.volunnear.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerProfileResponseDTO {
    private String username;
    private String realName;
//    private List<String> preferences;
//    private ActivitiesDTO activitiesDTO;
}

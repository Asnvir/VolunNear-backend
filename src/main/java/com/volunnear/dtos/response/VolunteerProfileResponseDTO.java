package com.volunnear.dtos.response;

import com.volunnear.dtos.enums.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerProfileResponseDTO {
    private UUID id;
    private String email;
    private String username;
    private String realName;
    private String avatarUrl;
    private List<ActivityType> preferences;
    private List<ActivitiesDTO> activitiesDTO;
}

package com.volunnear.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ActivitiesDTO {
    private List<ActivityDTO> activities;
    private OrganisationResponseDTO organisationResponseDTO;

    public ActivitiesDTO() {
        activities = new ArrayList<>();
    }

    public void addActivity(ActivityDTO activity) {
        activities.add(activity);
    }
}

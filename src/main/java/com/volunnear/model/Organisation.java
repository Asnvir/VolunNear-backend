package com.volunnear.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Organisation{
    private int organisationId;
    private String organisationName;
    private List<Activity> activities;
}

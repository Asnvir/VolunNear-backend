package com.volunnear.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVolunteerInfoRequestDTO {
    private String email;
    private String realName;
}

package com.volunnear.dtos.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistrationVolunteerRequestDTO {
    private String username;
    private String password;
    private String realName;
}

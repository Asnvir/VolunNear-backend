package com.volunnear.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistrationRequest {
    private String username;
    private String password;
    private String realName;
}

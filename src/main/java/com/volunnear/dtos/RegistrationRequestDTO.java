package com.volunnear.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistrationRequestDTO {
    private String username;
    private String password;
    private String realName;
}

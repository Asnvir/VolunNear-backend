package com.volunnear.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Volunteer {
    private String nameOfUser;
    private Credentials credentials;

}

package com.volunnear.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePassword {
    private String newPassword;

    private String repeatedNewPassword;

    public boolean arePasswordsEqual() {
        log.info("\nChecking if passwords are equal.\nNew password: {}\nRepeated new password: {}", newPassword, repeatedNewPassword);
        return newPassword != null && newPassword.equals(repeatedNewPassword);
    }
}

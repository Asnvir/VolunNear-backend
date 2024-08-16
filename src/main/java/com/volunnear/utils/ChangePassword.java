package com.volunnear.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePassword {
    private String newPassword;
    private String repeatedNewPassword;

    public boolean arePasswordsEqual() {
        return newPassword != null && newPassword.equals(repeatedNewPassword);
    }
}

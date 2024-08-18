package com.volunnear.dtos.forgotPassword;

import lombok.Builder;

@Builder
public record MailBody(String to, String subject, String text) {

}

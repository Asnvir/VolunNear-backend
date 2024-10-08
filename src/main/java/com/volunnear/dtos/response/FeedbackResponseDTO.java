package com.volunnear.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponseDTO {
    private UUID id;
    private double rate;
    private String description;
    private String realNameOfUser;
    private String username;
    private String avatarUrl;
}

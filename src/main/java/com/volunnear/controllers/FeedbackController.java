package com.volunnear.controllers;

import com.volunnear.services.activities.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;
}

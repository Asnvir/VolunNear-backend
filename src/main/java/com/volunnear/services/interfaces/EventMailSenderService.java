package com.volunnear.services.interfaces;

import com.volunnear.events.ActivityCreationEvent;
import jakarta.mail.MessagingException;

public interface EventMailSenderService {

    void sendNotificationForSubscribers(ActivityCreationEvent activityCreationEvent);
    void sendOtpEmail(String email, Integer otp) throws MessagingException;
}

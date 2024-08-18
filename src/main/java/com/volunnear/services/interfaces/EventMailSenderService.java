package com.volunnear.services.interfaces;

import com.volunnear.events.ActivityCreationEvent;

public interface EventMailSenderService {

    void sendNotificationForSubscribers(ActivityCreationEvent activityCreationEvent);
}

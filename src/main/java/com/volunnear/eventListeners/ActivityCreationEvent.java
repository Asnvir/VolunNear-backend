package com.volunnear.eventListeners;

import com.volunnear.dtos.ActivityNotificationDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ActivityCreationEvent extends ApplicationEvent {
    private final ActivityNotificationDTO notificationDTO;

    public ActivityCreationEvent(Object source, ActivityNotificationDTO notificationDTO) {
        super(source);
        this.notificationDTO = notificationDTO;
    }
}

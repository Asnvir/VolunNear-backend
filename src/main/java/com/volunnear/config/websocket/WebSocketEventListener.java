package com.volunnear.config.websocket;

import com.volunnear.services.interfaces.UserAvailabilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class WebSocketEventListener {

    private final UserAvailabilityService userAvailabilityService;

    private final Map<String,String> simpSessionIdToSubscriptionId;

    public WebSocketEventListener(UserAvailabilityService userAvailabilityService) {
        this.userAvailabilityService = userAvailabilityService;
        this.simpSessionIdToSubscriptionId = new ConcurrentHashMap<>();
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        userAvailabilityService.removeOnlineUser(event.getUser());
    }

    @EventListener
    @SendToUser
    public void handleSessionSubscribeEvent(SessionSubscribeEvent sessionSubscribeEvent) {
        String subscribedChannel = (String) sessionSubscribeEvent.getMessage().getHeaders().get("simpDestination");
        String simpSessionId = (String) sessionSubscribeEvent.getMessage().getHeaders().get("simpSessionId");
        if(subscribedChannel == null) {
            log.error("Subscribed channel is null");
            return;
        }
        simpSessionIdToSubscriptionId.put(simpSessionId, subscribedChannel);
        userAvailabilityService.addUserSubscribed(sessionSubscribeEvent.getUser(), subscribedChannel);
    }

    @EventListener
    public void handleUnSubscribeEvent(SessionUnsubscribeEvent sessionUnsubscribeEvent) {
        String simpSessionId = (String) sessionUnsubscribeEvent.getMessage().getHeaders().get("simpSessionId");
        String unsubscribedChannel = simpSessionIdToSubscriptionId.get(simpSessionId);
        userAvailabilityService.removeUserSubscribed(sessionUnsubscribeEvent.getUser(), unsubscribedChannel);
    }

    @EventListener
    public void handleConnectedEvent(SessionConnectedEvent sessionConnectedEvent) {
        userAvailabilityService.addOnlineUser(sessionConnectedEvent.getUser());
    }

}

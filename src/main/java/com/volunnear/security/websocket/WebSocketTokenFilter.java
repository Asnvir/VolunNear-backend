package com.volunnear.security.websocket;

import com.volunnear.security.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WebSocketTokenFilter implements ChannelInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public WebSocketTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT == accessor.getCommand()) {
            String token = jwtTokenProvider.resolveToken(accessor);
            try {
                if (token != null && jwtTokenProvider.validateToken(token)) {
                    Authentication auth = jwtTokenProvider.getAuthentication(token);
                    log.info("Auth: " + auth);
                    accessor.setUser(auth);
                } else {
                    throw new IllegalArgumentException("Invalid or missing token");
                }
            } catch (Exception e) {
                log.error("Error during WebSocket authentication: ", e);
                return null;
            }
        }
        return message;
    }
}

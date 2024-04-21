package com.volunnear.security.websocket;

import com.volunnear.security.jwt.JwtTokenProvider;
import com.volunnear.services.users.UserService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class WebSocketTokenFilter implements ChannelInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public WebSocketTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT == accessor.getCommand()) {
            String token = accessor.getFirstNativeHeader("Authorization");
            if (jwtTokenProvider.validateToken(token)) {
                Authentication usernamePasswordAuthenticationToken = jwtTokenProvider.getAuthentication(token);
                accessor.setUser(usernamePasswordAuthenticationToken);
            }
        }
        return message;
    }
}

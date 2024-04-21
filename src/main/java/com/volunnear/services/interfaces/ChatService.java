package com.volunnear.services.interfaces;


import com.volunnear.entitiy.chat.ChatMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public interface ChatService {

    ChatMessage sendMessageToConvId(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor, @DestinationVariable("convId") String conversationId);
}

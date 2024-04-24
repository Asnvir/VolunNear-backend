package com.volunnear.services.interfaces;


import com.volunnear.dtos.ChatMessageDTO;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public interface ChatService {

    ChatMessageDTO sendMessageToConvId(@Payload ChatMessageDTO chatMessageDTO, SimpMessageHeaderAccessor headerAccessor, @DestinationVariable("convId") String conversationId);
}

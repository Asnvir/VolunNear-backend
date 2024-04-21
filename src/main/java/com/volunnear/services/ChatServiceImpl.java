package com.volunnear.services;

import com.volunnear.entitiy.chat.ChatMessage;
import com.volunnear.services.interfaces.ChatService;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {
    @Override
    public ChatMessage sendMessageToConvId(ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor, String conversationId) {
        return null;
    }
}

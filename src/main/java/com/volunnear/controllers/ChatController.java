package com.volunnear.controllers;

import com.volunnear.entitiy.chat.ChatMessage;
import com.volunnear.repositories.chat.ChatMessageRepository;
import com.volunnear.services.interfaces.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat/sendMessage/{convId}")
    public ChatMessage sendMessageToConvId(ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor, String conversationId) {
        chatService.sendMessageToConvId(chatMessage, headerAccessor, conversationId);
        return chatMessage;
    }

//    @GetMapping("/messages")
//    public Page<ChatMessage> getMessagesBetween(
//            @RequestParam String sender,
//            @RequestParam String receiver,
//            @PageableDefault(size = 50, sort ="id", direction = Sort.Direction.DESC) Pageable pageable
//            ) {
//        return chatMessageRepository.findChatMessagesBetween(sender, receiver, pageable);
//    }
}

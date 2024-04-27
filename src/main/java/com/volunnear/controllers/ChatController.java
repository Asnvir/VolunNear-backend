package com.volunnear.controllers;

import com.volunnear.dtos.ChatMessageDTO;
import com.volunnear.services.interfaces.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat/sendMessage/{convId}")
    public ChatMessageDTO sendMessageToConvId(ChatMessageDTO chatMessageDTO, SimpMessageHeaderAccessor headerAccessor, String conversationId) {
        log.info("Received message: {}", chatMessageDTO);
        chatService.sendMessageToConvId(chatMessageDTO, headerAccessor, conversationId);
        return chatMessageDTO;
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

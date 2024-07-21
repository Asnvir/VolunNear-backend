package com.volunnear.services;

import com.volunnear.dtos.enums.MessageDeliveryStatus;
import com.volunnear.dtos.ChatMessageDTO;
import com.volunnear.entitiy.chat.ConversationEntity;
import com.volunnear.dtos.CustomUserDetails;
import com.volunnear.repositories.chat.ConversationRepository;
import com.volunnear.services.interfaces.ChatService;
import com.volunnear.services.interfaces.UserAvailabilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    private final ConversationRepository conversationRepository;

    private final UserAvailabilityService userAvailabilityService;

    public ChatServiceImpl(SimpMessageSendingOperations simpMessageSendingOperations, ConversationRepository conversationRepository, UserAvailabilityService userAvailabilityService) {
        this.simpMessageSendingOperations = simpMessageSendingOperations;
        this.conversationRepository = conversationRepository;
        this.userAvailabilityService = userAvailabilityService;
    }

    @Override
    public ChatMessageDTO sendMessageToConvId(ChatMessageDTO chatMessageDTO, SimpMessageHeaderAccessor headerAccessor, String conversationId) {
        CustomUserDetails userDetails = getUser(headerAccessor);
        setSenderDetails(chatMessageDTO, userDetails);
        boolean isTargetUserOnline = checkUserOnlineStatus(chatMessageDTO.getReceiverId());
        boolean isTargetUserSubscribed = checkUserSubscription(chatMessageDTO.getReceiverId(), conversationId);
        ConversationEntity conversationEntity = buildConversationEntity(
                chatMessageDTO,
                userDetails.getId(),
                conversationId,
                isTargetUserOnline,
                isTargetUserSubscribed);
        conversationRepository.save(conversationEntity);
        sendMessage(chatMessageDTO, conversationId, isTargetUserOnline, isTargetUserSubscribed);
        return chatMessageDTO;
    }

    private void setSenderDetails(ChatMessageDTO chatMessageDTO, CustomUserDetails userDetails) {
        chatMessageDTO.setSenderId(userDetails.getId());
        chatMessageDTO.setSenderName(userDetails.getUsername());
    }

    private ConversationEntity buildConversationEntity(ChatMessageDTO chatMessageDTO, UUID fromUserId, String conversationId, boolean isTargetOnline, boolean isTargetSubscribed) {
        ConversationEntity.ConversationEntityBuilder builder = ConversationEntity.builder()
                .id(chatMessageDTO.getId())
                .fromUser(fromUserId)
                .toUser(chatMessageDTO.getReceiverId())
                .content(chatMessageDTO.getContent())
                .lastModified(Timestamp.from(Instant.now()))
                .time(Timestamp.from(Instant.now()))
                .convId(conversationId)
                .deliveryStatus(determineDeliveryStatus(isTargetOnline, isTargetSubscribed));

        return builder.build();
    }

    private CustomUserDetails getUser(SimpMessageHeaderAccessor headerAccessor) {
        Authentication authentication = (Authentication) headerAccessor.getUser();
        log.info("Authentication: {}", authentication);
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return (CustomUserDetails) authentication.getPrincipal();
        }
        return null;
    }

    private boolean checkUserOnlineStatus(UUID userId) {
        return userAvailabilityService.isUserOnline(userId);
    }

    private boolean checkUserSubscription(UUID userId, String conversationId) {
        return userAvailabilityService.isUserSubscribed(userId, "/topic/" + conversationId);
    }

    private String determineDeliveryStatus(boolean isTargetOnline, boolean isTargetSubscribed) {
        if (!isTargetOnline) {
            return MessageDeliveryStatus.NOT_DELIVERED.toString();
        } else if (!isTargetSubscribed) {
            return MessageDeliveryStatus.DELIVERED.toString();
        } else {
            return MessageDeliveryStatus.SEEN.toString();
        }
    }

    private void sendMessage(ChatMessageDTO chatMessage, String conversationId, boolean isTargetOnline, boolean isTargetSubscribed) {
        if (!isTargetOnline) {
            log.info("{} is not online. Content saved in unseen messages", chatMessage.getReceiverName());
        } else if (!isTargetSubscribed) {
            log.info("{} is online but not subscribed. Sending to their private subscription", chatMessage.getSenderName());
            simpMessageSendingOperations.convertAndSend("/topic/" + chatMessage.getReceiverId().toString(), chatMessage);
        } else {
            simpMessageSendingOperations.convertAndSend("/topic/" + conversationId, chatMessage);
        }
    }
}

package com.volunnear.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDTO {

    private UUID id;


    private UUID senderId;
    private String senderName;


    private UUID receiverId;
    private String receiverName;


    private String content;

    public MessageDeliveryStatus deliveryStatus;

}

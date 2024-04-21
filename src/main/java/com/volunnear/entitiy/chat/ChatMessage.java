package com.volunnear.entitiy.chat;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender", nullable = false, length = 50)
    private String from;

    @Column(name = "receiver", nullable = false, length = 50)
    private String to;

    @Column(name = "message", nullable = false, length = 2048)
    private String text;
}

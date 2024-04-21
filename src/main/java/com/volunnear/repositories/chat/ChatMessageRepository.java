package com.volunnear.repositories.chat;

import com.volunnear.entitiy.chat.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT m from ChatMessage m WHERE (m.from = :sender AND m.to = :recipient) OR (m.from = :recipient AND m.to = :sender) ORDER BY m.id ASC")
    Page<ChatMessage> findChatMessagesBetween(String sender, String recipient, Pageable pageable);
}

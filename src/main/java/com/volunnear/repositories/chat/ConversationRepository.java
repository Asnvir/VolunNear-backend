package com.volunnear.repositories.chat;

import com.volunnear.entitiy.chat.ConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConversationRepository extends JpaRepository<ConversationEntity, UUID>{

}

package com.volunnear.repositories.infos;

import com.volunnear.entitiy.infos.ActivityChatLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ActivityChatLinkRepository extends JpaRepository<ActivityChatLink, UUID> {
    Optional<ActivityChatLink> findByActivity_Id(UUID id);

    boolean existsByActivity_Id(UUID id);
}
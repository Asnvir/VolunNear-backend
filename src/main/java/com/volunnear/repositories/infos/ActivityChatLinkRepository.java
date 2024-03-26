package com.volunnear.repositories.infos;

import com.volunnear.entitiy.infos.ActivityChatLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivityChatLinkRepository extends JpaRepository<ActivityChatLink, Long> {
    Optional<ActivityChatLink> findByActivity_Id(Long id);

    boolean existsByActivity_Id(Long id);
}
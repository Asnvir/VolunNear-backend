package com.volunnear.entitiy.infos;

import com.volunnear.entitiy.activities.Activity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "activity_chat_link")
public class ActivityChatLink {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private UUID id;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @Column(name = "link")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String link;
}
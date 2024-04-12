package com.volunnear.entitiy.infos;

import com.volunnear.entitiy.activities.Activity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "activity_chat_link")
public class ActivityChatLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @Column(name = "link")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String link;
}
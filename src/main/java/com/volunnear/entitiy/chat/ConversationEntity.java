package com.volunnear.entitiy.chat;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "conversation")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ConversationEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "conv_id")
    private String convId;

    @Column(name = "from_user")
    private UUID fromUser;

    @Column(name = "to_user")
    private UUID toUser;

    @Column(name = "time")
    @CreatedDate
    private Timestamp time;

    @Column(name = "last_modified")
    @LastModifiedDate
    private Timestamp lastModified;

    @Column(name = "content")
    private String content;

    @Column(name = "delivery_status")
    private String deliveryStatus;
}

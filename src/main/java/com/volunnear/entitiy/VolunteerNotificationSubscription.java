package com.volunnear.entitiy;

import com.volunnear.entitiy.users.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "volunteer_notification_subscription")
public class VolunteerNotificationSubscription {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_volunteer_id")
    private AppUser userVolunteer;

    @ManyToOne
    @JoinColumn(name = "user_organisation_id")
    private AppUser userOrganisation;
}
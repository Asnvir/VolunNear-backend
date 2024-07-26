package com.volunnear.entitiy.infos;

import com.volunnear.entitiy.users.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "volunteer_info")
public class VolunteerInfo {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "app_user_volunteer_id")
    private AppUser appUser;

    @Column(name = "real_name_of_user")
    private String realNameOfUser;

    @Column(name = "avatar_url", length = 1024)
    private String avatarUrl;
}
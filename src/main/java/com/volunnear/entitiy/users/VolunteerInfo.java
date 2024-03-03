package com.volunnear.entitiy.users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "volunteer_info")
public class VolunteerInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "app_user_volunteer_id")
    private AppUser appUser;

    @Column(name = "real_name_of_user")
    private String realNameOfUser;
}
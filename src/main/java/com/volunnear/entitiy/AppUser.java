package com.volunnear.entitiy;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "app_user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false, length = 150)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "authority", nullable = false, length = 150)
    private String authority;

}
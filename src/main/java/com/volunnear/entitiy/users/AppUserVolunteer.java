package com.volunnear.entitiy.users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "app_users_volunteers")
public class AppUserVolunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "username", nullable = false, length = 150)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "real_name", nullable = false)
    private String realName;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "volunteers_roles",
            joinColumns = @JoinColumn(name = "appUser_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id"))
    private List<Role> roles;

}
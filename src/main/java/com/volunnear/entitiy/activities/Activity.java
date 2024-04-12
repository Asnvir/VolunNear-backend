package com.volunnear.entitiy.activities;

import com.volunnear.entitiy.users.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@Entity
@ToString
@RequiredArgsConstructor
@Table(name = "activities")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "kind_of_activity")
    private String kindOfActivity;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_place")
    private Date dateOfPlace;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;
}
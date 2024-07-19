package com.volunnear.entitiy.activities;

import com.volunnear.entitiy.users.AppUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "activities")
public class Activity {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "number_of_house")
    private String numberOfHouse;

    @Column(name = "kind_of_activity")
    private String kindOfActivity;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_place")
    private Date dateOfPlace;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;
}
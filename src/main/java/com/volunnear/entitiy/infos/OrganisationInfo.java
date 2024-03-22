package com.volunnear.entitiy.infos;

import com.volunnear.entitiy.users.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "organisation_info")
public class OrganisationInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "app_user_organisation_id", unique = true)
    private AppUser appUser;

    @Column(name = "name_of_organistaion")
    private String nameOfOrganisation;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;
}
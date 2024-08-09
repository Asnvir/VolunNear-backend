package com.volunnear.entitiy.infos;

import com.volunnear.entitiy.OrganisationRating;
import com.volunnear.entitiy.users.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.Set;
import java.util.UUID;
@Getter
@Setter
@Entity
@Table(name = "organisation_info")
public class OrganisationInfo {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private UUID id;

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

    @Column(name = "avatar_url", length = 1024)
    private String avatarUrl;

    @OneToMany(mappedBy = "organisation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<OrganisationRating> ratings;

    public double getAverageRating() {
        return ratings.stream()
                .mapToDouble(OrganisationRating::getRating)
                .average()
                .orElse(0.0);
    }
}
package com.volunnear.entitiy;

import com.volunnear.entitiy.infos.OrganisationInfo;
import com.volunnear.entitiy.users.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "organisation_rating", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"organisation_id", "user_id"})
})
public class OrganisationRating {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "organisation_id", nullable = false)
    private OrganisationInfo organisation;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(name = "rating", nullable = false)
    private double rating;
}
package com.volunnear.entitiy.infos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "feedback_about_organisation")
public class FeedbackAboutOrganisation {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name_of_volunteer")
    private String nameOfVolunteer;

    @Column(name = "username_of_volunteer")
    private String usernameOfVolunteer;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "organisation_info_id")
    private OrganisationInfo organisationInfo;
}
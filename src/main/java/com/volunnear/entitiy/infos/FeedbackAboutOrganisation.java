package com.volunnear.entitiy.infos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "feedback_about_organisation")
public class FeedbackAboutOrganisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name_of_volunteer")
    private String nameOfVolunteer;

    @Column(name = "username_of_volunteer")
    private String usernameOfVolunteer;

    @Column(name = "rate")
    private Integer rate;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "organisation_info_id")
    private OrganisationInfo organisationInfo;
}
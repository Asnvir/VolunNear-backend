package com.volunnear.entitiy.infos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "organisation_group_link")
public class OrganisationGroupLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "organisation_info_id")
    private OrganisationInfo organisationInfo;

    @Column(name = "link")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String link;
}
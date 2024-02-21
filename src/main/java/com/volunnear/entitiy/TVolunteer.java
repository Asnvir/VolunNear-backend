package com.volunnear.entitiy;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "t_volunteer")
public class TVolunteer {
    @Id
    @Column(name = "id_volunteer", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_volunteer", nullable = false)
    private AppUser appUser;

    @Column(name = "real_name", length = 45)
    private String realName;

}
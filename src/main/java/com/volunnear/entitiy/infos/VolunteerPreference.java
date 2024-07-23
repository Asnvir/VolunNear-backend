package com.volunnear.entitiy.infos;

import com.volunnear.dtos.enums.ActivityType;
import com.volunnear.entitiy.users.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "volunteer_preference")
public class VolunteerPreference {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    private AppUser volunteer;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "preferences")
    @CollectionTable(name = "volunteer_preferences_list", joinColumns = @JoinColumn(name = "owner_id"))
    private List<ActivityType> preferences = new ArrayList<>();

    public void addPreferences(List<ActivityType> preferences) {
        this.preferences.addAll(preferences);
    }
}
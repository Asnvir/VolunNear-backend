package com.volunnear.repository;

import com.volunnear.entitiy.TVolunteer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerRepository extends JpaRepository<TVolunteer, Long> {
}

package com.volunnear.repositories;

import com.volunnear.entitiy.AppUserVolunteer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUserVolunteer, Long> {
    Optional<AppUserVolunteer> findAppUserByUsername(String username);

    boolean existsAppUserByUsername(String username);
}

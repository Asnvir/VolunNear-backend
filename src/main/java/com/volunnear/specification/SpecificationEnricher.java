package com.volunnear.specification;

import com.volunnear.dtos.enums.ActivityType;
import com.volunnear.entitiy.activities.Activity;
import com.volunnear.entitiy.activities.VolunteerInActivity;
import com.volunnear.entitiy.users.AppUser;
import com.volunnear.services.users.UserService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

@Component
public class SpecificationEnricher {
    private final UserService userService;

    public SpecificationEnricher(UserService userService) {
        this.userService = userService;
    }

    /**
     * Creates the specification for filtering activities based on given parameters.
     */
    public Specification<Activity> createSpecification(
            String title,
            String description,
            String country,
            String city,
            ActivityType kindOfActivity,
            Date dateOfPlace) {


        return createBaseSpecification(
                title, description, country, city, kindOfActivity, dateOfPlace
        );
    }

    /**
     * Creates a base specification for filtering activities.
     */
    public Specification<Activity> createBaseSpecification(
            String title,
            String description,
            String country,
            String city,
            ActivityType kindOfActivity,
            Date dateOfPlace) {

        return Specification.where(ActivitySpecification.hasTitle(title))
                .and(ActivitySpecification.hasDescription(description))
                .and(ActivitySpecification.hasCountry(country))
                .and(ActivitySpecification.hasCity(city))
                .and(ActivitySpecification.hasKindOfActivity(kindOfActivity))
                .and(ActivitySpecification.hasDateOfPlace(dateOfPlace));
    }

    public Specification<VolunteerInActivity> createVolunteerInActivitySpecification(
            String title,
            String description,
            String country,
            String city,
            ActivityType kindOfActivity,
            Date dateOfPlace,
            AppUser appUser)
     {
       return Specification.where(VolunteerInActivitySpecification.hasActivityTitle(title))
                .and(VolunteerInActivitySpecification.hasActivityDescription(description))
                .and(VolunteerInActivitySpecification.hasActivityCountry(country))
                .and(VolunteerInActivitySpecification.hasActivityCity(city))
                .and(VolunteerInActivitySpecification.hasActivityKindOfActivity(kindOfActivity))
                .and(VolunteerInActivitySpecification.hasActivityDateOfPlace(dateOfPlace))
                .and(VolunteerInActivitySpecification.hasAppUser(appUser));

    }

}

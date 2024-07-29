package com.volunnear.specification;

import com.volunnear.dtos.enums.ActivityType;
import com.volunnear.dtos.enums.SortOrder;
import com.volunnear.entitiy.activities.Activity;
import com.volunnear.entitiy.activities.VolunteerInActivity;
import com.volunnear.entitiy.infos.OrganisationInfo;
import com.volunnear.entitiy.users.AppUser;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class SpecificationEnricher {

    /**
     * Creates the specification for filtering activities based on given parameters.
     */
    public Specification<Activity> createSpecification(
            String title,
            String description,
            String country,
            String city,
            ActivityType kindOfActivity,
            LocalDate dateOfPlace) {


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
            LocalDate dateOfPlace) {

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
            LocalDate dateOfPlace,
            AppUser appUser) {
        return Specification.where(VolunteerInActivitySpecification.hasActivityTitle(title))
                .and(VolunteerInActivitySpecification.hasActivityDescription(description))
                .and(VolunteerInActivitySpecification.hasActivityCountry(country))
                .and(VolunteerInActivitySpecification.hasActivityCity(city))
                .and(VolunteerInActivitySpecification.hasActivityKindOfActivity(kindOfActivity))
                .and(VolunteerInActivitySpecification.hasActivityDateOfPlace(dateOfPlace))
                .and(VolunteerInActivitySpecification.hasAppUser(appUser));

    }

    public Specification<OrganisationInfo> createOrganisationInfoSpecification(
            String nameOfOrganisation,
            String country,
            String city,
            SortOrder sortOrder) {
        List<String> fields = List.of("nameOfOrganisation", "country", "city");
        return Specification.where(OrganisationSpecification.hasName(nameOfOrganisation))
                .and(OrganisationSpecification.hasCountry(country))
                .and(OrganisationSpecification.hasCity(city)
                        .and(OrganisationSpecification.sortBy(fields, sortOrder)));
    }

}

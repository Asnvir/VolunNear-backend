package com.volunnear.specification;

import com.volunnear.dtos.enums.ActivityType;
import com.volunnear.entitiy.activities.Activity;
import com.volunnear.entitiy.activities.VolunteerInActivity;
import com.volunnear.entitiy.users.AppUser;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class VolunteerInActivitySpecification {
    public static Specification<VolunteerInActivity> hasActivityTitle(String title) {
        return (Root<VolunteerInActivity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (title == null || title.isEmpty()) {
                return cb.conjunction();
            }
            Join<VolunteerInActivity, Activity> activityJoin = root.join("activity");
            return cb.like(cb.lower(activityJoin.get("title")), "%" + title.toLowerCase() + "%");
        };
    }

    public static Specification<VolunteerInActivity> hasActivityDescription(String description) {
        return (Root<VolunteerInActivity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (description == null || description.isEmpty()) {
                return cb.conjunction();
            }
            Join<VolunteerInActivity, Activity> activityJoin = root.join("activity");
            return cb.like(cb.lower(activityJoin.get("description")), "%" + description.toLowerCase() + "%");
        };
    }

    public static Specification<VolunteerInActivity> hasActivityCountry(String country) {
        return (Root<VolunteerInActivity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (country == null || country.isEmpty()) {
                return cb.conjunction();
            }
            Join<VolunteerInActivity, Activity> activityJoin = root.join("activity");
            return cb.equal(activityJoin.get("country"), country);
        };
    }

    public static Specification<VolunteerInActivity> hasActivityCity(String city) {
        return (Root<VolunteerInActivity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (city == null || city.isEmpty()) {
                return cb.conjunction();
            }
            Join<VolunteerInActivity, Activity> activityJoin = root.join("activity");
            return cb.equal(activityJoin.get("city"), city);
        };
    }

    public static Specification<VolunteerInActivity> hasActivityKindOfActivity(ActivityType kindOfActivity) {
        return (Root<VolunteerInActivity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (kindOfActivity == null) {
                return cb.conjunction();
            }
            Join<VolunteerInActivity, Activity> activityJoin = root.join("activity");
            return cb.equal(activityJoin.get("kindOfActivity"), kindOfActivity);
        };
    }

    public static Specification<VolunteerInActivity> hasActivityDateOfPlace(Date dateOfPlace) {
        return (Root<VolunteerInActivity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (dateOfPlace == null) {
                return cb.conjunction();
            }
            Join<VolunteerInActivity, Activity> activityJoin = root.join("activity");
            return cb.equal(activityJoin.get("dateOfPlace"), dateOfPlace);
        };
    }

    public static Specification<VolunteerInActivity> hasAppUser(AppUser appUser) {
        return (root, query, criteriaBuilder) ->
                appUser == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("user"), appUser);
    }
}

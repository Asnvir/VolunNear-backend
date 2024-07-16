package com.volunnear.specification;

import com.volunnear.entitiy.activities.Activity;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class ActivitySpecification {
    public static Specification<Activity> hasTitle(String title) {
        return (root, query, criteriaBuilder) ->
                title == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("title"), title);
    }

    public static Specification<Activity> hasDescription(String description) {
        return (root, query, criteriaBuilder) ->
                description == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("description"), description);
    }

    public static Specification<Activity> hasCountry(String country) {
        return (root, query, criteriaBuilder) ->
                country == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("country"), country);
    }

    public static Specification<Activity> hasCity(String city) {
        return (root, query, criteriaBuilder) ->
                city == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("city"), city);
    }

    public static Specification<Activity> hasKindOfActivity(String kindOfActivity) {
        return (root, query, criteriaBuilder) ->
                kindOfActivity == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("kindOfActivity"), kindOfActivity);
    }

    public static Specification<Activity> hasDateOfPlace(Date dateOfPlace) {
        return (root, query, criteriaBuilder) ->
                dateOfPlace == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("dateOfPlace"), dateOfPlace);
    }
}

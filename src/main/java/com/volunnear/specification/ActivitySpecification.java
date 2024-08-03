package com.volunnear.specification;

import com.volunnear.dtos.enums.ActivityType;
import com.volunnear.entitiy.activities.Activity;
import com.volunnear.entitiy.users.AppUser;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Calendar;
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

    public static Specification<Activity> hasKindOfActivity(ActivityType kindOfActivity) {
        return (root, query, criteriaBuilder) ->
                kindOfActivity == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("kindOfActivity"), kindOfActivity);
    }

    public static Specification<Activity> hasDateOfPlace(LocalDate dateOfPlace) {
        return (root, query, criteriaBuilder) -> {
            if (dateOfPlace == null) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Date today = calendar.getTime();

                return criteriaBuilder.greaterThan(root.get("dateOfPlace"), today);
            }

            return criteriaBuilder.and(
                    criteriaBuilder.equal(criteriaBuilder.function("year", Integer.class, root.get("dateOfPlace")), dateOfPlace.getYear()),
                    criteriaBuilder.equal(criteriaBuilder.function("month", Integer.class, root.get("dateOfPlace")), dateOfPlace.getMonthValue()),
                    criteriaBuilder.equal(criteriaBuilder.function("day", Integer.class, root.get("dateOfPlace")), dateOfPlace.getDayOfMonth())
            );
        };
    }

    public static Specification<Activity> hasAppUser(AppUser appUser) {
        return (root, query, criteriaBuilder) ->
                appUser == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("appUser"), appUser);
    }
}

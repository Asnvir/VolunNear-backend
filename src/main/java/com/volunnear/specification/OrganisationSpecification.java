package com.volunnear.specification;

import com.volunnear.dtos.enums.SortOrder;
import com.volunnear.entitiy.infos.OrganisationInfo;
import jakarta.persistence.criteria.Order;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;

public class OrganisationSpecification {

    public static Specification<OrganisationInfo> hasName(String nameOfOrganisation) {
        return (root, query, criteriaBuilder) ->
                nameOfOrganisation == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("nameOfOrganisation"), nameOfOrganisation);
    }

    public static Specification<OrganisationInfo> hasCountry(String country) {
        return (root, query, criteriaBuilder) ->
                country == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("country"), country);
    }

    public static Specification<OrganisationInfo> hasCity(String city) {
        return (root, query, criteriaBuilder) ->
                city == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("city"), city);
    }

    public static Specification<OrganisationInfo> sortBy(List<String> fields, SortOrder sortOrder) {
        return (root, query, criteriaBuilder) -> {

                List<Order> orders = fields.stream()
                        .map(field -> sortOrder == SortOrder.ASC
                                ? criteriaBuilder.asc(root.get(field))
                                : criteriaBuilder.desc(root.get(field)))
                        .toList();
                query.orderBy(orders);
            return criteriaBuilder.conjunction();
        };
    }

}

package com.backend.housing.infrastructure.persistence.specifications;

import com.backend.housing.domain.entity.properties.enums.PropertyStatus;
import com.backend.housing.domain.entity.properties.valueObjects.SearchPropertyQuery;
import com.backend.housing.infrastructure.persistence.entities.properties.PropertyEntity;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class PropertySpecification {

    private PropertySpecification() {}

    public static Specification<PropertyEntity> withFilters(SearchPropertyQuery query) {

        return (root, queryBuilder, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (query.hasCity()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("address").get("city")),
                                "%" + query.getCity().toLowerCase() + "%"
                        )
                );
            }

            if (query.hasMinPrice()) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("rentPrice"),
                                query.getMinPrice()
                        )
                );
            }

            if (query.hasMaxPrice()) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("rentPrice"),
                                query.getMaxPrice()
                        )
                );
            }

            if (query.hasTypeProperty()) {
                predicates.add(
                        cb.equal(
                                root.get("typeProperty"),
                                query.getTypeProperty()
                        )
                );
            }



            if (query.hasBedrooms()) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("numberOfBedrooms"),
                                query.getBedrooms()
                        )
                );
            }

            if (query.hasPetsAllowed()) {
                predicates.add(
                        cb.equal(
                                root.get("petsAllowed"),
                                query.getPetsAllowed()
                        )
                );
            }

            if (query.hasFurnished()) {
                predicates.add(
                        cb.equal(
                                root.get("furnished"),
                                query.getFurnished()
                        )
                );
            }

            predicates.add(
                    cb.equal(
                            root.get("propertyStatus"),
                            PropertyStatus.PUBLISHED
                    )
            );

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
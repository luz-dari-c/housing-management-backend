package com.backend.housing.infrastructure.persistence.adapters.properties;



import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.ports.out.PropertySearchRepository;
import com.backend.housing.domain.entity.properties.valueObjects.SearchPropertyQuery;
import com.backend.housing.infrastructure.persistence.entities.properties.PropertyEntity;
import com.backend.housing.infrastructure.persistence.repositories.properties.JpaPropertyRepository;
import com.backend.housing.infrastructure.persistence.specifications.PropertySpecification;
import com.backend.housing.infrastructure.persistence.mappers.PropertyEntityMapper;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PropertySearchRepositoryAdapter implements PropertySearchRepository {

    private final JpaPropertyRepository jpaPropertyRepository;
    private final PropertyEntityMapper propertyEntityMapper;

    public PropertySearchRepositoryAdapter(
            JpaPropertyRepository jpaPropertyRepository,
            PropertyEntityMapper propertyEntityMapper
    ) {
        this.jpaPropertyRepository = jpaPropertyRepository;
        this.propertyEntityMapper = propertyEntityMapper;
    }

    @Override
    public List<Property> searchProperties(SearchPropertyQuery query) {

        Specification<PropertyEntity> specification =
                PropertySpecification.withFilters(query);

        List<PropertyEntity> entities =
                jpaPropertyRepository.findAll(specification);

        return entities
                .stream()
                .map(propertyEntityMapper::toDomain)
                .collect(Collectors.toList());
    }
}

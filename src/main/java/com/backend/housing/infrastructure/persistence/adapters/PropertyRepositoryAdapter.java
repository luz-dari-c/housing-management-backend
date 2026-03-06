package com.backend.housing.infrastructure.persistence.adapters;


import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.ports.out.PropertyRepository;
import com.backend.housing.infrastructure.persistence.entities.PropertyEntity;
import com.backend.housing.infrastructure.persistence.mappers.PropertyEntityMapper;
import com.backend.housing.infrastructure.persistence.repositories.JpaPropertyRepository;
import jakarta.persistence.Id;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class PropertyRepositoryAdapter  implements PropertyRepository {


    private final JpaPropertyRepository jpaPropertyRepository;
    private final PropertyEntityMapper mapper;

    public PropertyRepositoryAdapter(JpaPropertyRepository repository, PropertyEntityMapper mapper){
        this.jpaPropertyRepository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Property save(Property property){

        if (property == null){

            throw new IllegalArgumentException("Property cannot be null");
        }
        PropertyEntity entity = mapper.toEntity(property);

        PropertyEntity savedEntity = jpaPropertyRepository.save(entity);
        return mapper.toDomain(savedEntity);

    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Property> findById(PropertyId id){

        if (id== null){
            return Optional.empty();
        }

        Long idValue = id.getValue();
        Optional<PropertyEntity> optionalEntity = jpaPropertyRepository.findById(idValue);

        return optionalEntity.map(mapper::toDomain);
    }


}

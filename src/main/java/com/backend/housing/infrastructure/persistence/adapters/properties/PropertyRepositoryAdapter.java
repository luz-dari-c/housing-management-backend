package com.backend.housing.infrastructure.persistence.adapters.properties;


import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.ports.out.PropertyRepository;
import com.backend.housing.domain.valueObjects.Pagination;
import com.backend.housing.infrastructure.persistence.entities.properties.PropertyEntity;
import com.backend.housing.infrastructure.persistence.mappers.PropertyEntityMapper;
import com.backend.housing.infrastructure.persistence.repositories.properties.JpaPropertyRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import java.util.List;
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

    @Override
    @Transactional(readOnly = true)
    public List<Property> findAll(Pagination pagination) {

        Pageable pageable = PageRequest.of(
                pagination.getPage(),
                pagination.getSize()
        );

        return jpaPropertyRepository
                .findAll(pageable)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

}

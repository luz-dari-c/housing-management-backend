package com.backend.housing.infrastructure.persistence.repositories.properties;


import com.backend.housing.domain.entity.properties.PropertyStatus;
import com.backend.housing.infrastructure.persistence.entities.properties.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

@Repository
public interface JpaPropertyRepository
        extends JpaRepository<PropertyEntity, Long>, JpaSpecificationExecutor<PropertyEntity> {

    List<PropertyEntity> findByPropertyStatus(PropertyStatus status);
}

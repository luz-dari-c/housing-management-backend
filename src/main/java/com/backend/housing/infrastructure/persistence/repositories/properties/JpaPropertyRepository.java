package com.backend.housing.infrastructure.persistence.repositories.properties;

import com.backend.housing.domain.entity.properties.enums.PropertyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.backend.housing.domain.entity.properties.enums.PropertyStatus;
import com.backend.housing.infrastructure.persistence.entities.properties.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaPropertyRepository
        extends JpaRepository<PropertyEntity, UUID>, JpaSpecificationExecutor<PropertyEntity> {

    List<PropertyEntity> findByPropertyStatus(PropertyStatus status);

    Page<PropertyEntity> findByPropertyStatus(PropertyStatus status, Pageable pageable);

    Page<PropertyEntity> findByOwnerIdAndPropertyStatus(Long ownerId, PropertyStatus status, Pageable pageable);

    Page<PropertyEntity> findByOwnerId(Long ownerId, Pageable pageable);

}

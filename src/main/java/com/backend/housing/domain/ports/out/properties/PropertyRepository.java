package com.backend.housing.domain.ports.out.properties;


import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.enums.PropertyStatus;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.valueobjects.Pagination;

import java.util.List;
import java.util.Optional;

public interface PropertyRepository {

    Property save (Property property);

    Optional<Property> findById(PropertyId id);

    List<Property> findAll(Pagination pagination);


    List<Property> findByPropertyStatus(PropertyStatus status, Pagination pagination);

    List<Property> findByOwnerIdAndPropertyStatus(Long ownerId, PropertyStatus status, Pagination pagination);
    List<Property> findByOwnerId(Long ownerId, Pagination pagination);


}

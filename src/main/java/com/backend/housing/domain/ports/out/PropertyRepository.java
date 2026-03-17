package com.backend.housing.domain.ports.out;


import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.valueObjects.Pagination;

import java.util.List;
import java.util.Optional;

public interface PropertyRepository {

    Property save (Property property);

    Optional<Property> findById(PropertyId id);

    List<Property> findAll(Pagination pagination);





}

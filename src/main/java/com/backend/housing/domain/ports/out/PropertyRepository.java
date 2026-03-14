package com.backend.housing.domain.ports.out;


import java.util.Optional;

import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;

public interface PropertyRepository {

    Property save (Property property);

    Optional<Property> findById(PropertyId id);





}

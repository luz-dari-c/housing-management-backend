package com.backend.housing.domain.ports.out;


import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;

import java.util.Optional;

public interface PropertyRepository {

    Property save (Property property);

    Optional<Property> findById(PropertyId id);





}

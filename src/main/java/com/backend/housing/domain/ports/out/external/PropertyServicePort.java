package com.backend.housing.domain.ports.out.external;


import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;

import java.util.Optional;

public interface PropertyServicePort {


    void markAsRented(PropertyId propertyId);

    void markAsAvailable(PropertyId propertyId);

    boolean isAvailableForRent(PropertyId propertyId);

    Optional<Property> getPropertyBasicInfo(PropertyId propertyId);

}

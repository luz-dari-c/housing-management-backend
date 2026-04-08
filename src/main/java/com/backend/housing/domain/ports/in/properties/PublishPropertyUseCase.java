package com.backend.housing.domain.ports.in.properties;


import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;

public interface PublishPropertyUseCase {

        void publish(PropertyId propertyId);


}

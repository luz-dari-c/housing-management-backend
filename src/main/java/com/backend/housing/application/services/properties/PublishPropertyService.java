package com.backend.housing.application.services.properties;

import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.ports.in.properties.PublishPropertyUseCase;
import com.backend.housing.domain.ports.out.properties.PropertyRepository;
import org.springframework.stereotype.Service;

@Service
public class PublishPropertyService implements PublishPropertyUseCase {

    private final PropertyRepository propertyRepository;

    public PublishPropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public void publish(PropertyId propertyId) {

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        property.publish();

        propertyRepository.save(property);
    }
}
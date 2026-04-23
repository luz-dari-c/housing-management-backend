package com.backend.housing.infrastructure.persistence.adapters.properties;


import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.ports.in.properties.GetPropertyUseCase;
import com.backend.housing.domain.ports.in.properties.UpdatePropertyUseCase;
import com.backend.housing.domain.ports.out.external.PropertyServicePort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PropertyServiceAdapter implements PropertyServicePort {

    private final GetPropertyUseCase getPropertyUseCase;
    private final UpdatePropertyUseCase updatePropertyUseCase;

    public PropertyServiceAdapter(GetPropertyUseCase getPropertyUseCase,
                                  UpdatePropertyUseCase updatePropertyUseCase) {
        this.getPropertyUseCase = getPropertyUseCase;
        this.updatePropertyUseCase = updatePropertyUseCase;
    }

    @Override
    public void markAsRented(PropertyId propertyId) {
        Property property = getPropertyUseCase.getProperty(propertyId);
        property.markAsRented();
        updatePropertyUseCase.updateFromDomain(property);
    }

    @Override
    public void markAsAvailable(PropertyId propertyId) {
        Property property = getPropertyUseCase.getProperty(propertyId);
        property.markAsAvailable();
        updatePropertyUseCase.updateFromDomain(property);
    }

    @Override
    public boolean isAvailableForRent(PropertyId propertyId) {
        Property property = getPropertyUseCase.getProperty(propertyId);
        return property.isAvailableForRent();
    }

    @Override
    public Optional<Property> getPropertyBasicInfo(PropertyId propertyId) {
        try {
            Property property = getPropertyUseCase.getProperty(propertyId);
            return Optional.of(property);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
package com.backend.housing.application.services.properties;

import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.exceptions.InvalidNotFoundException;
import com.backend.housing.domain.ports.in.properties.DisablePropertyUseCase;
import com.backend.housing.domain.ports.out.properties.PropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DisablePropertyService implements DisablePropertyUseCase {

    private final PropertyRepository propertyRepository;

    public DisablePropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public void execute(PropertyId propertyId, Long requestingUserId) {

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new InvalidNotFoundException(
                        "Propiedad no encontrada: " + propertyId
                ));

        if (!property.isOwnedBy(requestingUserId)) {
            throw new SecurityException("No tienes permiso para deshabilitar esta propiedad");
        }

        property.disable();

        propertyRepository.save(property);
    }
}
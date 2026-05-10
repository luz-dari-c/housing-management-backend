package com.backend.housing.domain.ports.in.properties;

import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;

public interface DeletePropertyUseCase {
    void execute(PropertyId propertyId, Long requestingUserId);
}
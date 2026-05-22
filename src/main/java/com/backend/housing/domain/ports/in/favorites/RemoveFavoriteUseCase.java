package com.backend.housing.domain.ports.in.favorites;


import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;

public interface RemoveFavoriteUseCase {
    void execute(Long userId, PropertyId propertyId);

}

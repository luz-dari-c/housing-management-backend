package com.backend.housing.domain.ports.in.properties;


import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;

public interface DisablePropertyUseCase {

    void execute (PropertyId id, Long requestingUserId);
}

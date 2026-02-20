package com.backend.housing.domain.ports.in.properties;

import com.backend.housing.application.Commands.properties.CreatePropertyCommand;
import com.backend.housing.domain.entity.properties.Property;
;

public interface CreatePropertyUseCase {

    Property createProperty( CreatePropertyCommand command);
}

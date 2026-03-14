package com.backend.housing.domain.ports.in.properties;


import com.backend.housing.application.Commands.properties.UpdatePropertyCommand;
import com.backend.housing.domain.entity.properties.Property;

public interface UpdatePropertyUseCase {

    Property update(UpdatePropertyCommand command);

}

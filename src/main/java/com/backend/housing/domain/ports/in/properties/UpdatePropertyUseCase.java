package com.backend.housing.domain.ports.in.properties;


import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.application.commands.properties.UpdatePropertyCommand;

public interface UpdatePropertyUseCase {

    Property update(UpdatePropertyCommand command);
    Property updateFromDomain(Property property);


}

package com.backend.housing.domain.ports.in.properties;


import com.backend.housing.domain.entity.properties.Property;

import java.util.List;
import java.util.UUID;

public interface ListMyAcquisitionsUseCase {

    List<Property> listPropertiesIBougth(UUID UserId);
    List<Property> listPropertiesIRented(UUID UserId);
}

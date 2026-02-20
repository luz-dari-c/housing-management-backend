package com.backend.housing.domain.ports.in.properties;

import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;

public interface ArchivePropertyUseCase {

    Property archive(PropertyId id);


}

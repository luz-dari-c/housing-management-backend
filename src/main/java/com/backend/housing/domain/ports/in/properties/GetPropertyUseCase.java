package com.backend.housing.domain.ports.in.properties;


import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;

import java.util.List;
import java.util.UUID;

public interface GetPropertyUseCase {

    Property getProperty(PropertyId id, UUID requestingUserId);

}

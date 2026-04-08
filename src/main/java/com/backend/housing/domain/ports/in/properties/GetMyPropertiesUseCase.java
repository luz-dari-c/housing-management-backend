package com.backend.housing.domain.ports.in.properties;


import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.enums.PropertyStatus;
import com.backend.housing.domain.valueobjects.Pagination;

import java.util.List;

public interface GetMyPropertiesUseCase {

    List<Property> getMyProperties(Pagination pagination, PropertyStatus status);
}

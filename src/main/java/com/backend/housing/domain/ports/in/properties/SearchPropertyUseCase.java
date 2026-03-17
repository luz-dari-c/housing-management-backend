package com.backend.housing.domain.ports.in.properties;

import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.SearchPropertyQuery;

import java.util.List;


public interface SearchPropertyUseCase {
    List<Property> searchProperties(SearchPropertyQuery query);

}

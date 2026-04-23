package com.backend.housing.domain.ports.out.properties;

import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.SearchPropertyQuery;

import java.util.List;

public interface PropertySearchRepository {

    List<Property> searchProperties(SearchPropertyQuery query);

}

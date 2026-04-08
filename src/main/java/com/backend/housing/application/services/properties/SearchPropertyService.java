package com.backend.housing.application.services.properties;


import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.SearchPropertyQuery;
import com.backend.housing.domain.ports.in.properties.SearchPropertyUseCase;
import com.backend.housing.domain.ports.out.properties.PropertySearchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchPropertyService implements SearchPropertyUseCase {

    private final PropertySearchRepository propertySearchRepository;

    public SearchPropertyService(PropertySearchRepository propertySearchRepository) {
        this.propertySearchRepository = propertySearchRepository;
    }

    @Override
    public List<Property> searchProperties(SearchPropertyQuery query) {

        return propertySearchRepository.searchProperties(query);

    }
}
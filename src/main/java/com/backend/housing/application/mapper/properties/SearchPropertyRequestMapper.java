package com.backend.housing.application.mapper.properties;



import com.backend.housing.application.dto.request.properties.SearchPropertyRequest;
import com.backend.housing.domain.entity.properties.valueObjects.SearchPropertyQuery;

public class SearchPropertyRequestMapper {

    private SearchPropertyRequestMapper() {
    }

    public static SearchPropertyQuery toQuery(SearchPropertyRequest request) {

        if (request == null) {
            return null;
        }

        return new SearchPropertyQuery(
                request.getCity(),
                request.getMinPrice(),
                request.getMaxPrice(),
                request.getTypeProperty(),
                request.getRentType(),
                request.getBedrooms(),
                request.getPetsAllowed(),
                request.getFurnished()
        );
    }
}

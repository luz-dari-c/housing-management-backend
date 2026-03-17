package com.backend.housing.application.mapper.properties;


import com.backend.housing.application.dto.response.properties.PropertyDetailResponse;
import com.backend.housing.application.dto.response.properties.PropertySummaryResponse;
import com.backend.housing.domain.entity.properties.Property;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PropertyResponseMapper {

    public PropertyDetailResponse toResponse(Property property) {

        if (property == null) {
            return null;
        }

        return new PropertyDetailResponse(
                property.getTitle(),
                property.getId().getValue(),
                property.getDescription(),
                property.getCoordinates(),
                property.getSalePrice(),
                property.getRentPrice(),
                property.getStatus(),
                property.getTypeProperty(),
                property.getOwnerId(),
                property.getUpdatedAt(),
                property.getPublishedAt(),
                property.getImageUrls(),
                property.getNumberOfBedrooms(),
                property.getNumberOfBathrooms(),
                property.isPetsAllowed(),
                property.getAreaInSquareMeters(),
                property.isFurnished(),
                property.getAddress()



        );
    }

    public static List<PropertySummaryResponse> toResponseList(List<Property> properties) {

        return properties
                .stream()
                .map(PropertySummaryResponseMapper::toResponse)
                .toList();
    }
}

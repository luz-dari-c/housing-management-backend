package com.backend.housing.application.mapper.properties;

import com.backend.housing.application.dto.response.properties.PropertySummaryResponse;
import com.backend.housing.domain.entity.properties.Property;

import java.util.List;

public class PropertySummaryResponseMapper {

    public static PropertySummaryResponse toResponse(Property property) {
        if (property == null) {
            return null;
        }

        return new PropertySummaryResponse(
                property.getId().getValue(),
                property.getTitle(),
                property.getDescription(),
                property.getTransactionType(),
                property.getPriceAmount(),
                property.getImageUrls()
        );
    }

    public static List<PropertySummaryResponse> toResponseList(List<Property> properties) {
        if (properties == null) {
            return List.of();
        }

        return properties
                .stream()
                .map(PropertySummaryResponseMapper::toResponse)
                .toList();
    }
}
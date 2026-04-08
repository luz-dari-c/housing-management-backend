package com.backend.housing.application.mapper.properties;

import com.backend.housing.application.dto.response.properties.CreatePropertyResponse;
import com.backend.housing.application.dto.response.properties.PropertyDetailResponse;
import com.backend.housing.application.dto.response.properties.PropertySummaryResponse;
import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.RentalTerms;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PropertyResponseMapper {

    public CreatePropertyResponse toCreateResponse(Property property) {
        return new CreatePropertyResponse(
                property.getId().getValue(),
                property.getTitle(),
                property.getDescription(),
                property.getCoordinates(),
                property.getTransactionType(),
                property.getPriceAmount(),
                property.getTypeProperty(),
                property.getStatus(),
                property.getOwnerId(),
                property.getCreatedAt(),
                property.getImageUrls(),
                property.getAddress()
        );
    }

    public PropertyDetailResponse toDetailResponse(Property property) {

        RentalTerms rentalTerms = property.getRentalTerms();

        Boolean petsAllowed = rentalTerms != null ? rentalTerms.isPetsAllowed() : null;
        Boolean furnished = rentalTerms != null ? rentalTerms.isFurnished() : null;

        return new PropertyDetailResponse(
                property.getId().getValue(),
                property.getTitle(),
                property.getDescription(),
                property.getCoordinates(),
                property.getTransactionType(),
                property.getPriceAmount(),
                property.getTypeProperty(),
                property.getStatus(),
                property.getOwnerId(),
                property.getUpdatedAt(),
                property.getPublishedAt(),
                property.getImageUrls(),
                property.getNumberOfBedrooms(),
                property.getNumberOfBathrooms(),
                property.getAreaInSquareMeters(),
                petsAllowed,
                furnished,
                property.getAddress()
        );
    }

    public PropertySummaryResponse toSummaryResponse(Property property) {
        return new PropertySummaryResponse(
                property.getId().getValue(),
                property.getTitle(),
                property.getDescription(),
                property.getTransactionType(),
                property.getPriceAmount(),
                property.getImageUrls()
        );
    }

    public List<PropertySummaryResponse> toSummaryResponseList(List<Property> properties) {
        if (properties == null) {
            return List.of();
        }
        return properties.stream()
                .map(this::toSummaryResponse)
                .collect(Collectors.toList());
    }
}
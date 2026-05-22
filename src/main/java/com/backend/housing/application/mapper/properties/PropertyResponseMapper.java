package com.backend.housing.application.mapper.properties;

import com.backend.housing.application.dto.response.properties.CreatePropertyResponse;
import com.backend.housing.application.dto.response.properties.PropertyDetailResponse;
import com.backend.housing.application.dto.response.properties.PropertySummaryResponse;
import com.backend.housing.application.dto.response.properties.UpdatePropertyResponse;
import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.RentalTerms;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PropertyResponseMapper {

    public CreatePropertyResponse toCreateResponse(Property property) {
        String message = "Propiedad creada exitosamente";
        String nextStep = "Publica la propiedad para que aparezca en los resultados de búsqueda";

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
                property.getAddress(),
                message,
                nextStep
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
                property.getCreatedAt(),
                property.getUpdatedAt(),
                property.getPublishedAt(),
                property.getImageUrls(),
                property.getNumberOfBedrooms(),
                property.getNumberOfBathrooms(),
                property.getAreaInSquareMeters(),
                petsAllowed,
                furnished,
                property.getAddress(),
                rentalTerms != null ? rentalTerms.getPaymentFrequency() : null

        );
    }

    public PropertySummaryResponse toSummaryResponse(Property property) {
        return new PropertySummaryResponse(
                property.getId().getValue(),
                property.getTitle(),
                property.getDescription(),
                property.getTransactionType(),
                property.getPriceAmount(),
                property.getImageUrls(),
                property.getStatus()
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

    public UpdatePropertyResponse toUpdateResponse(Property property) {
        String message = "Propiedad actualizada exitosamente";
        String nextStep = "Los cambios se verán reflejados inmediatamente en la plataforma";

        String street = null, city = null, state = null, country = null, postalCode = null;
        if (property.getAddress() != null) {
            street = property.getAddress().getStreet();
            city = property.getAddress().getCity();
            state = property.getAddress().getState();
            country = property.getAddress().getCountry();
            postalCode = property.getAddress().getPostalCode();
        }

        BigDecimal latitude = null, longitude = null;
        if (property.getCoordinates() != null) {
            latitude = property.getCoordinates().getLatitud();
            longitude = property.getCoordinates().getLongitud();
        }

        Boolean petsAllowed = null;
        Boolean furnished = null;
        if (property.getRentalTerms() != null) {
            petsAllowed = property.getRentalTerms().isPetsAllowed();
            furnished = property.getRentalTerms().isFurnished();
        }

        return new UpdatePropertyResponse(
                property.getId().getValue(),
                property.getTitle(),
                property.getDescription(),
                property.getTransactionType(),
                property.getPriceAmount(),
                property.getTypeProperty(),
                property.getStatus(),
                property.getNumberOfBedrooms(),
                property.getNumberOfBathrooms(),
                property.getAreaInSquareMeters(),
                petsAllowed,
                furnished,
                property.getImageUrls(),
                street, city, state, country, postalCode,
                latitude, longitude,
                property.getUpdatedAt(),
                message,
                nextStep
        );
    }
}
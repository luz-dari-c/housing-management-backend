package com.backend.housing.application.dto.response.properties;

import com.backend.housing.domain.entity.properties.enums.PropertyStatus;
import com.backend.housing.domain.entity.properties.enums.TransactionType;
import com.backend.housing.domain.entity.properties.enums.TypeProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UpdatePropertyResponse(
        UUID id,
        String title,
        String description,
        TransactionType transactionType,
        BigDecimal priceAmount,
        TypeProperty typeProperty,
        PropertyStatus status,
        Integer numberOfBedrooms,
        Integer numberOfBathrooms,
        Integer areaInSquareMeters,
        Boolean petsAllowed,
        Boolean furnished,
        List<String> imageUrls,
        String street,
        String city,
        String state,
        String country,
        String postalCode,
        BigDecimal latitude,
        BigDecimal longitude,
        LocalDateTime updatedAt,
        String message,
        String nextStep
) {}
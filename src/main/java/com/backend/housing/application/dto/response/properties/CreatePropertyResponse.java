package com.backend.housing.application.dto.response.properties;

import com.backend.housing.domain.entity.properties.enums.PropertyStatus;
import com.backend.housing.domain.entity.properties.enums.TransactionType;
import com.backend.housing.domain.entity.properties.enums.TypeProperty;
import com.backend.housing.domain.entity.properties.valueObjects.Address;
import com.backend.housing.domain.entity.properties.valueObjects.Coordinates;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreatePropertyResponse(
        UUID id,
        String title,
        String description,
        Coordinates coordinates,
        TransactionType transactionType,
        BigDecimal priceAmount,
        TypeProperty typeProperty,
        PropertyStatus status,
        Long ownerId,
        LocalDateTime createdAt,
        List<String> imageUrls,
        Address address,
        String message,
        String nextStep
) {}
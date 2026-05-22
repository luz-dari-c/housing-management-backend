package com.backend.housing.application.dto.response.rentalcontracts;

import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.rentalcontracts.Enums.RentalRequestStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RentalRequestResponse(
        String requestId,
        PropertyId propertyId,
        Long tenantId,
        Long ownerId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal proposedRent,
        RentalRequestStatus status,
        LocalDateTime createdAt,
        LocalDateTime respondedAt,
        String message,
        String nextStep
) {}
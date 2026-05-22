package com.backend.housing.application.dto.response.rentalcontracts;

import com.backend.housing.domain.entity.properties.enums.PaymentFrequency;
import com.backend.housing.domain.entity.rentalcontracts.Enums.ContractStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ContractResponse(
        UUID contractId,
        UUID propertyId,
        String propertyTitle,
        Long tenantId,
        String tenantName,
        String tenantCedula,
        Long ownerId,
        String ownerName,
        String ownerCedula,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal periodRent,
        ContractStatus status,
        LocalDateTime createdAt,
        PaymentFrequency paymentFrequency
) {}
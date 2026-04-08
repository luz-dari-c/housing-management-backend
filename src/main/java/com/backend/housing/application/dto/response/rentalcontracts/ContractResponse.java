package com.backend.housing.application.dto.response.rentalcontracts;

import com.backend.housing.domain.entity.rentalcontracts.Enums.ContractStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class ContractResponse {

    private final UUID contractId;
    private final UUID propertyId;
    private final String propertyTitle;
    private final Long tenantId;
    private final String tenantName;
    private final Long ownerId;
    private final String ownerName;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final BigDecimal monthlyRent;
    private final ContractStatus status;
    private final LocalDateTime createdAt;

    public ContractResponse(
            UUID contractId,
            UUID propertyId,
            String propertyTitle,
            Long tenantId,
            String tenantName,
            Long ownerId,
            String ownerName,
            LocalDate startDate,
            LocalDate endDate,
            BigDecimal monthlyRent,
            ContractStatus status,
            LocalDateTime createdAt
    ) {
        this.contractId = contractId;
        this.propertyId = propertyId;
        this.propertyTitle = propertyTitle;
        this.tenantId = tenantId;
        this.tenantName = tenantName;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.monthlyRent = monthlyRent;
        this.status = status;
        this.createdAt = createdAt;
    }
}
package com.backend.housing.application.dto.response.rentalcontracts;

import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.rentalcontracts.Enums.RentalRequestStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class RentalRequestResponse {

    private String requestId;
    private PropertyId propertyId;
    private Long tenantId;
    private Long ownerId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal proposedRent;
    private RentalRequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime respondedAt;

}
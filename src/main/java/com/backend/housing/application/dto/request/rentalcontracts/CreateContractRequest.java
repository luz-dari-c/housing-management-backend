package com.backend.housing.application.dto.request.rentalcontracts;

import com.backend.housing.domain.entity.properties.enums.PaymentFrequency;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CreateContractRequest {

    @NotNull(message = "Property ID is required")
    private UUID propertyId;

    @NotNull(message = "Tenant ID is required")
    private Long tenantId;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date cannot be in the past")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotNull(message = "Monthly rent is required")
    @DecimalMin(value = "0.01", message = "Monthly rent must be greater than zero")
    private BigDecimal monthlyRent;

    @NotNull(message = "Payment frequency is required")
    private PaymentFrequency paymentFrequency;

    @AssertTrue(message = "End date must be after start date")
    public boolean isDateRangeValid() {
        if (startDate == null || endDate == null) return true;
        return endDate.isAfter(startDate);
    }
}
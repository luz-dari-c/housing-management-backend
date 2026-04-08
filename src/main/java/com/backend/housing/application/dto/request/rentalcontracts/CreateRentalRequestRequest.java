package com.backend.housing.application.dto.request.rentalcontracts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateRentalRequestRequest {

    @NotNull(message = "Property ID is required")
    @NotBlank(message = "Property ID cannot be blank")
    private String propertyId;

    @NotNull(message = "Proposed rent is required")
    @Positive(message = "Proposed rent must be positive")
    private BigDecimal proposedRent;

    @NotNull(message = "Start date is required")
    private String startDate;

    @NotNull(message = "End date is required")
    private String endDate;
}

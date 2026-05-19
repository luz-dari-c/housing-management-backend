package com.backend.housing.application.commands.rentalcontracts;

import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Getter
public class CreateRentalRequestCommand {

    private final PropertyId propertyId;
    private final Long tenantId;
    private final LocalDate startDate;
    private final Integer duration;
    private final BigDecimal proposedRent;

    public CreateRentalRequestCommand(PropertyId propertyId,
                                      Long tenantId,
                                      LocalDate startDate,
                                      Integer duration,
                                      BigDecimal proposedRent) {

        this.propertyId = Objects.requireNonNull(propertyId, "PropertyId is required");
        this.tenantId = Objects.requireNonNull(tenantId, "TenantId is required");
        this.startDate = Objects.requireNonNull(startDate, "StartDate is required");
        this.duration = Objects.requireNonNull(duration, "Duration is required");
        this.proposedRent = proposedRent;

        validateStartDate();
    }

    private void validateStartDate() {
        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }
    }
}
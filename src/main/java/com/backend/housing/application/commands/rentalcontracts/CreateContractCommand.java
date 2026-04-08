package com.backend.housing.application.commands.rentalcontracts;

import com.backend.housing.domain.entity.properties.enums.PaymentFrequency;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Getter
public class CreateContractCommand {

    private final PropertyId propertyId;
    private final Long tenantId;
    private final Long ownerId;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final BigDecimal monthlyRent;
    private final PaymentFrequency paymentFrequency;

    private CreateContractCommand(Builder builder) {
        this.propertyId = Objects.requireNonNull(builder.propertyId, "PropertyId is required");
        this.tenantId = Objects.requireNonNull(builder.tenantId, "TenantId is required");
        this.ownerId = Objects.requireNonNull(builder.ownerId, "OwnerId is required");
        this.startDate = Objects.requireNonNull(builder.startDate, "StartDate is required");
        this.endDate = Objects.requireNonNull(builder.endDate, "EndDate is required");
        this.monthlyRent = Objects.requireNonNull(builder.monthlyRent, "Monthly rent is required");
        this.paymentFrequency = Objects.requireNonNull(builder.paymentFrequency, "Payment frequency is required");

        validateDates();
        validateTenantAndOwnerAreDifferent();
    }

    private void validateDates() {
        if (!endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }
    }

    private void validateTenantAndOwnerAreDifferent() {
        if (tenantId.equals(ownerId)) {
            throw new IllegalArgumentException("Tenant and owner cannot be the same person");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private PropertyId propertyId;
        private Long tenantId;
        private Long ownerId;
        private LocalDate startDate;
        private LocalDate endDate;
        private BigDecimal monthlyRent;
        private PaymentFrequency paymentFrequency;

        public Builder propertyId(PropertyId propertyId) { this.propertyId = propertyId; return this; }
        public Builder tenantId(Long tenantId) { this.tenantId = tenantId; return this; }
        public Builder ownerId(Long ownerId) { this.ownerId = ownerId; return this; }
        public Builder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public Builder endDate(LocalDate endDate) { this.endDate = endDate; return this; }

        public Builder monthlyRent(BigDecimal monthlyRent) {
            this.monthlyRent = monthlyRent;
            return this;
        }

        public Builder paymentFrequency(PaymentFrequency paymentFrequency) {
            this.paymentFrequency = paymentFrequency;
            return this;
        }

        public CreateContractCommand build() {
            return new CreateContractCommand(this);
        }
    }
}
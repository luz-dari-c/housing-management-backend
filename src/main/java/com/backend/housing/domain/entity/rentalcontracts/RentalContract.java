package com.backend.housing.domain.entity.rentalcontracts;

import com.backend.housing.domain.entity.properties.enums.PaymentFrequency;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.rentalcontracts.Enums.ContractStatus;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.DateRange;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.MonthlyRent;

import java.time.LocalDateTime;
import java.util.Objects;

public class RentalContract {

    private final ContractId id;
    private final PropertyId propertyId;
    private final Long tenantId;
    private final Long ownerId;
    private final DateRange period;
    private final MonthlyRent monthlyRent;

    private ContractStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime terminatedAt;

    private RentalContract(ContractId id,
                           PropertyId propertyId,
                           Long tenantId,
                           Long ownerId,
                           DateRange period,
                           MonthlyRent monthlyRent,
                           ContractStatus status,
                           LocalDateTime createdAt,
                           LocalDateTime terminatedAt) {

        this.id = Objects.requireNonNull(id);
        this.propertyId = Objects.requireNonNull(propertyId);
        this.tenantId = Objects.requireNonNull(tenantId);
        this.ownerId = Objects.requireNonNull(ownerId);
        this.period = Objects.requireNonNull(period);
        this.monthlyRent = Objects.requireNonNull(monthlyRent);
        this.status = Objects.requireNonNull(status);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.terminatedAt = terminatedAt;

        validateUsers();
    }

    public static RentalContract create(PropertyId propertyId,
                                        Long tenantId,
                                        Long ownerId,
                                        DateRange period,
                                        MonthlyRent monthlyRent) {

        return new RentalContract(
                ContractId.generate(),
                propertyId,
                tenantId,
                ownerId,
                period,
                monthlyRent,
                ContractStatus.ACTIVE,
                LocalDateTime.now(),
                null
        );
    }

    public static RentalContract reconstitute(ContractId id,
                                              PropertyId propertyId,
                                              Long tenantId,
                                              Long ownerId,
                                              DateRange period,
                                              MonthlyRent monthlyRent,
                                              ContractStatus status,
                                              LocalDateTime createdAt,
                                              LocalDateTime terminatedAt) {

        return new RentalContract(
                id,
                propertyId,
                tenantId,
                ownerId,
                period,
                monthlyRent,
                status,
                createdAt,
                terminatedAt
        );
    }

    private void validateUsers() {
        if (tenantId.equals(ownerId)) {
            throw new IllegalArgumentException();
        }
    }

    public void terminate() {
        if (status != ContractStatus.ACTIVE) {
            throw new IllegalStateException();
        }
        this.status = ContractStatus.TERMINATED;
        this.terminatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (status != ContractStatus.ACTIVE) {
            throw new IllegalStateException();
        }
        this.status = ContractStatus.CANCELLED;
        this.terminatedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return status == ContractStatus.ACTIVE && period.isActive();
    }

    public boolean belongsToTenant(Long userId) {
        return this.tenantId.equals(userId);
    }

    public boolean belongsToOwner(Long userId) {
        return this.ownerId.equals(userId);
    }

    public ContractId getId() { return id; }
    public PropertyId getPropertyId() { return propertyId; }
    public Long getTenantId() { return tenantId; }
    public Long getOwnerId() { return ownerId; }
    public DateRange getPeriod() { return period; }
    public MonthlyRent getMonthlyRent() { return monthlyRent; }
    public ContractStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getTerminatedAt() { return terminatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RentalContract that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
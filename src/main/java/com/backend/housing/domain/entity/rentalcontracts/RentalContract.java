package com.backend.housing.domain.entity.rentalcontracts;

import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.rentalcontracts.Enums.ContractStatus;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.DateRange;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.MonthlyRent;

import java.time.LocalDate;
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


    private LocalDate actualStartDate;


    private LocalDate paymentDueDate;

    private RentalContract(ContractId id,
                           PropertyId propertyId,
                           Long tenantId,
                           Long ownerId,
                           DateRange period,
                           MonthlyRent monthlyRent,
                           ContractStatus status,
                           LocalDateTime createdAt,
                           LocalDateTime terminatedAt,
                           LocalDate actualStartDate,
                           LocalDate paymentDueDate) {

        this.id =id;
        this.propertyId = Objects.requireNonNull(propertyId);
        this.tenantId = Objects.requireNonNull(tenantId);
        this.ownerId = Objects.requireNonNull(ownerId);
        this.period = Objects.requireNonNull(period);
        this.monthlyRent = Objects.requireNonNull(monthlyRent);
        this.status = Objects.requireNonNull(status);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.terminatedAt = terminatedAt;
        this.actualStartDate = actualStartDate;
        this.paymentDueDate = paymentDueDate;

        validateUsers();
    }

    public static RentalContract create(PropertyId propertyId,
                                        Long tenantId,
                                        Long ownerId,
                                        DateRange period,
                                        MonthlyRent monthlyRent) {

        return new RentalContract(
                ContractId.empty(),
                propertyId,
                tenantId,
                ownerId,
                period,
                monthlyRent,
                ContractStatus.PAYMENT_PENDING,
                LocalDateTime.now(),
                null,
                null,
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
                                              LocalDateTime terminatedAt,
                                              LocalDate actualStartDate,
                                              LocalDate paymentDueDate) {

        return new RentalContract(
                id,
                propertyId,
                tenantId,
                ownerId,
                period,
                monthlyRent,
                status,
                createdAt,
                terminatedAt,
                actualStartDate,
                paymentDueDate
        );
    }

     public void activate(LocalDate paymentConfirmedDate) {
        if (status != ContractStatus.PAYMENT_PENDING) {
            throw new IllegalStateException("Contract cannot be activated from status: " + status);
        }
        this.status = ContractStatus.ACTIVE;
        this.actualStartDate = paymentConfirmedDate;
        this.paymentDueDate = paymentConfirmedDate.plusMonths(1);
    }

    public void renewPaymentPeriod(LocalDate newPaymentConfirmedDate) {
        if (status != ContractStatus.ACTIVE) {
            throw new IllegalStateException("Only active contracts can renew payment period");
        }
        this.paymentDueDate = newPaymentConfirmedDate.plusMonths(1);
    }

    public void requestCancellation() {
        if (status != ContractStatus.ACTIVE) {
            throw new IllegalStateException("Only active contracts can request cancellation");
        }
        this.status = ContractStatus.CANCELLATION_PENDING;
    }

    public void cancel() {
        if (status != ContractStatus.CANCELLATION_PENDING) {
            throw new IllegalStateException("Contract must be in CANCELLATION_PENDING to be cancelled");
        }
        this.status = ContractStatus.CANCELLED;
        this.terminatedAt = LocalDateTime.now();
    }
 public void terminate() {
        if (status != ContractStatus.ACTIVE) {
            throw new IllegalStateException("Only active contracts can be terminated");
        }
        this.status = ContractStatus.TERMINATED;
        this.terminatedAt = LocalDateTime.now();
    }

     public void expire() {
        if (status != ContractStatus.ACTIVE && status != ContractStatus.CANCELLATION_PENDING) {
            throw new IllegalStateException("Contract cannot expire from status: " + status);
        }
        this.status = ContractStatus.EXPIRED;
        this.terminatedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return status == ContractStatus.ACTIVE && period.isActive();
    }

    public boolean requiresPayment() {
        return (status == ContractStatus.ACTIVE || status == ContractStatus.CANCELLATION_PENDING)
                && period.isActive();
    }

    public boolean belongsToTenant(Long userId) {
        return this.tenantId.equals(userId);
    }

    public boolean belongsToOwner(Long userId) {
        return this.ownerId.equals(userId);
    }

    private void validateUsers() {
        if (tenantId.equals(ownerId)) {
            throw new IllegalArgumentException("Tenant and owner cannot be the same user");
        }
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
    public LocalDate getActualStartDate() { return actualStartDate; }
    public LocalDate getPaymentDueDate() { return paymentDueDate; }

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
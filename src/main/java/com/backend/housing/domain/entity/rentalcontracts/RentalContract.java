package com.backend.housing.domain.entity.rentalcontracts;

import com.backend.housing.domain.entity.properties.enums.PaymentFrequency;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.rentalcontracts.Enums.ContractStatus;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.DateRange;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.PeriodRent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class RentalContract {

    private final ContractId id;
    private final PropertyId propertyId;
    private final Long tenantId;
    private final Long ownerId;
    private final DateRange period;
    private final PeriodRent periodRent;
    private ContractStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime terminatedAt;
    private LocalDate actualStartDate;
    private LocalDate paymentDueDate;
    private LocalDate effectiveCancellationDate;
    private final PaymentFrequency paymentFrequency;

    private RentalContract(ContractId id,
                           PropertyId propertyId,
                           Long tenantId,
                           Long ownerId,
                           DateRange period,
                           PeriodRent periodRent,
                           PaymentFrequency paymentFrequency,
                           ContractStatus status,
                           LocalDateTime createdAt,
                           LocalDateTime terminatedAt,
                           LocalDate actualStartDate,
                           LocalDate paymentDueDate,
                           LocalDate effectiveCancellationDate) {

        this.id = id;
        this.propertyId = Objects.requireNonNull(propertyId);
        this.tenantId = Objects.requireNonNull(tenantId);
        this.ownerId = Objects.requireNonNull(ownerId);
        this.period = Objects.requireNonNull(period);
        this.periodRent = Objects.requireNonNull(periodRent);
        this.paymentFrequency = Objects.requireNonNull(paymentFrequency);
        this.status = Objects.requireNonNull(status);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.terminatedAt = terminatedAt;
        this.actualStartDate = actualStartDate;
        this.paymentDueDate = paymentDueDate;
        this.effectiveCancellationDate = effectiveCancellationDate;

        validateUsers();
    }

    public static RentalContract create(PropertyId propertyId,
                                        Long tenantId,
                                        Long ownerId,
                                        DateRange period,
                                        PeriodRent periodRent,
                                        PaymentFrequency paymentFrequency) {

        return new RentalContract(
                ContractId.empty(),
                propertyId,
                tenantId,
                ownerId,
                period,
                periodRent,
                paymentFrequency,
                ContractStatus.PAYMENT_PENDING,
                LocalDateTime.now(),
                null,
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
                                              PeriodRent periodRent,
                                              PaymentFrequency paymentFrequency,
                                              ContractStatus status,
                                              LocalDateTime createdAt,
                                              LocalDateTime terminatedAt,
                                              LocalDate actualStartDate,
                                              LocalDate paymentDueDate,
                                              LocalDate effectiveCancellationDate) {

        return new RentalContract(
                id,
                propertyId,
                tenantId,
                ownerId,
                period,
                periodRent,
                paymentFrequency,
                status,
                createdAt,
                terminatedAt,
                actualStartDate,
                paymentDueDate,
                effectiveCancellationDate
        );
    }

    public void activate(LocalDate paymentConfirmedDate) {
        if (status != ContractStatus.PAYMENT_PENDING) {
            throw new IllegalStateException("Contract cannot be activated from status: " + status);
        }
        this.status = ContractStatus.ACTIVE;
        this.actualStartDate = this.period.getStartDate();

        this.paymentDueDate = switch (paymentFrequency) {
            case WEEKLY -> this.period.getStartDate().plusWeeks(1);
            case BIWEEKLY -> this.period.getStartDate().plusWeeks(2);
            case MONTHLY -> this.period.getStartDate().plusMonths(1);
        };
    }

    public void renewPaymentPeriod(LocalDate newPaymentConfirmedDate) {
        if (status != ContractStatus.ACTIVE && status != ContractStatus.CANCELLATION_PENDING) {
            throw new IllegalStateException("Only ACTIVE or CANCELLATION_PENDING contracts can renew payment period");
        }

        this.paymentDueDate = switch (paymentFrequency) {
            case WEEKLY -> this.paymentDueDate.plusWeeks(1);
            case BIWEEKLY -> this.paymentDueDate.plusWeeks(2);
            case MONTHLY -> this.paymentDueDate.plusMonths(1);
        };
    }

    public void cancelImmediately() {
        if (status != ContractStatus.PAYMENT_PENDING) {
            throw new IllegalStateException(
                    "cancelImmediately() solo aplica a contratos en PAYMENT_PENDING. Estado actual: " + status
            );
        }
        this.status = ContractStatus.CANCELLED;
        this.terminatedAt = LocalDateTime.now();
    }

    public void scheduleCancellation(LocalDate effectiveDate) {
        if (status != ContractStatus.ACTIVE) {
            throw new IllegalStateException(
                    "scheduleCancellation() solo aplica a contratos ACTIVE. Estado actual: " + status
            );
        }
        Objects.requireNonNull(effectiveDate, "La fecha efectiva de cancelación no puede ser nula");
        if (!effectiveDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha efectiva de cancelación debe ser futura");
        }
        this.effectiveCancellationDate = effectiveDate;
        this.status = ContractStatus.CANCELLATION_PENDING;
    }

    public void cancel() {
        if (status != ContractStatus.CANCELLATION_PENDING) {
            throw new IllegalStateException(
                    "cancel() solo aplica a contratos en CANCELLATION_PENDING. Estado actual: " + status
            );
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

    public boolean isCancellable() {
        return status == ContractStatus.PAYMENT_PENDING || status == ContractStatus.ACTIVE;
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

    public PaymentFrequency getPaymentFrequency() { return paymentFrequency; }
    public ContractId getId() { return id; }
    public PropertyId getPropertyId() { return propertyId; }
    public Long getTenantId() { return tenantId; }
    public Long getOwnerId() { return ownerId; }
    public DateRange getPeriod() { return period; }
    public PeriodRent getPeriodRent() { return periodRent; }
    public ContractStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getTerminatedAt() { return terminatedAt; }
    public LocalDate getActualStartDate() { return actualStartDate; }
    public LocalDate getPaymentDueDate() { return paymentDueDate; }
    public LocalDate getEffectiveCancellationDate() { return effectiveCancellationDate; }

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
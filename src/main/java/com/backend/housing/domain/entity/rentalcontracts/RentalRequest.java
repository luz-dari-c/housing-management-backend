package com.backend.housing.domain.entity.rentalcontracts;

import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.rentalcontracts.Enums.RentalRequestStatus;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.DateRange;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.RequestId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class RentalRequest {

    private final RequestId id;
    private final PropertyId propertyId;
    private final Long tenantId;
    private final Long ownerId;
    private final DateRange period;
    private final BigDecimal proposedRent;

    private RentalRequestStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime respondedAt;

    private RentalRequest(RequestId id,
                          PropertyId propertyId,
                          Long tenantId,
                          Long ownerId,
                          DateRange period,
                          BigDecimal proposedRent,
                          RentalRequestStatus status,
                          LocalDateTime createdAt,
                          LocalDateTime respondedAt) {

        this.id = Objects.requireNonNull(id);
        this.propertyId = Objects.requireNonNull(propertyId);
        this.tenantId = Objects.requireNonNull(tenantId);
        this.ownerId = Objects.requireNonNull(ownerId);
        this.period = Objects.requireNonNull(period);
        this.proposedRent = proposedRent;
        this.status = Objects.requireNonNull(status);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.respondedAt = respondedAt;

        validateUsers();
        validateProposedRent();
    }

    public static RentalRequest fromPersistence(
            RequestId id,
            PropertyId propertyId,
            Long tenantId,
            Long ownerId,
            DateRange period,
            BigDecimal proposedRent,
            RentalRequestStatus status,
            LocalDateTime createdAt,
            LocalDateTime respondedAt
    ) {
        return new RentalRequest(
                id,
                propertyId,
                tenantId,
                ownerId,
                period,
                proposedRent,
                status,
                createdAt,
                respondedAt
        );
    }

    public static RentalRequest create(PropertyId propertyId,
                                       Long tenantId,
                                       Long ownerId,
                                       DateRange period,
                                       BigDecimal proposedRent) {

        return new RentalRequest(
                RequestId.generate(),
                propertyId,
                tenantId,
                ownerId,
                period,
                proposedRent,
                RentalRequestStatus.PENDING,
                LocalDateTime.now(),
                null
        );
    }

    private void validateUsers() {
        if (tenantId.equals(ownerId)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateProposedRent() {
        if (proposedRent != null && proposedRent.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException();
        }
    }

    public void accept() {
        if (status != RentalRequestStatus.PENDING) {
            throw new IllegalStateException();
        }
        this.status = RentalRequestStatus.ACCEPTED;
        this.respondedAt = LocalDateTime.now();
    }

    public void reject() {
        if (status != RentalRequestStatus.PENDING) {
            throw new IllegalStateException();
        }
        this.status = RentalRequestStatus.REJECTED;
        this.respondedAt = LocalDateTime.now();
    }

    public void cancel(Long userId) {
        if (!tenantId.equals(userId)) {
            throw new SecurityException();
        }
        if (status != RentalRequestStatus.PENDING) {
            throw new IllegalStateException();
        }
        this.status = RentalRequestStatus.CANCELLED;
        this.respondedAt = LocalDateTime.now();
    }

    public boolean isPending() {
        return status == RentalRequestStatus.PENDING;
    }

    public RequestId getId() { return id; }
    public PropertyId getPropertyId() { return propertyId; }
    public Long getTenantId() { return tenantId; }
    public Long getOwnerId() { return ownerId; }
    public DateRange getPeriod() { return period; }
    public BigDecimal getProposedRent() { return proposedRent; }
    public RentalRequestStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getRespondedAt() { return respondedAt; }
}
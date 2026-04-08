package com.backend.housing.infrastructure.persistence.entities.rentalcontract;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "rental_requests")
public class RentalRequestEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String propertyId;

    @Column(nullable = false)
    private Long tenantId;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    private BigDecimal proposedRent;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime respondedAt;

    public RentalRequestEntity() {
    }

    public RentalRequestEntity(
            String id,
            String propertyId,
            Long tenantId,
            Long ownerId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            BigDecimal proposedRent,
            String status,
            LocalDateTime createdAt,
            LocalDateTime respondedAt
    ) {
        this.id = id;
        this.propertyId = propertyId;
        this.tenantId = tenantId;
        this.ownerId = ownerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.proposedRent = proposedRent;
        this.status = status;
        this.createdAt = createdAt;
        this.respondedAt = respondedAt;
    }

}
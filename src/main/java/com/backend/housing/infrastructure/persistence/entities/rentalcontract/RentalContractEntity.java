package com.backend.housing.infrastructure.persistence.entities.rentalcontract;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "rental_contracts")
public class RentalContractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "property_id", nullable = false)
    private UUID propertyId;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "monthly_rent", nullable = false)
    private BigDecimal monthlyRent;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "terminated_at")
    private LocalDateTime terminatedAt;

    public RentalContractEntity() {}

    public RentalContractEntity(UUID id, UUID propertyId, Long tenantId, Long ownerId,
                                LocalDate startDate, LocalDate endDate, BigDecimal monthlyRent,
                                 String status,
                                LocalDateTime createdAt, LocalDateTime terminatedAt) {
        this.id = id;
        this.propertyId = propertyId;
        this.tenantId = tenantId;
        this.ownerId = ownerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.monthlyRent = monthlyRent;
        this.status = status;
        this.createdAt = createdAt;
        this.terminatedAt = terminatedAt;
    }
}
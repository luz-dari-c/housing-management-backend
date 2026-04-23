package com.backend.housing.infrastructure.persistence.entities.payments;

import com.backend.housing.domain.entity.payments.enums.PaymentMethod;
import com.backend.housing.domain.entity.payments.enums.PaymentReferenceType;
import com.backend.housing.domain.entity.payments.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class PaymentEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "reference_id", nullable = false)
    private UUID referenceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type", nullable = false)
    private PaymentReferenceType referenceType;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false, length = 10)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false)
    private PaymentMethod method;

    // NUEVOS CAMPOS para Stripe Checkout Session
    @Column(name = "checkout_session_id")
    private String checkoutSessionId;

    @Column(name = "checkout_url", length = 500)
    private String checkoutUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    public PaymentEntity(UUID id,
                         UUID referenceId,
                         PaymentReferenceType referenceType,
                         BigDecimal amount,
                         String currency,
                         PaymentStatus status,
                         PaymentMethod method,
                         String checkoutSessionId,
                         String checkoutUrl,
                         LocalDateTime createdAt,
                         LocalDateTime paidAt) {
        this.id = id;
        this.referenceId = referenceId;
        this.referenceType = referenceType;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.method = method;
        this.checkoutSessionId = checkoutSessionId;
        this.checkoutUrl = checkoutUrl;
        this.createdAt = createdAt;
        this.paidAt = paidAt;
    }
}
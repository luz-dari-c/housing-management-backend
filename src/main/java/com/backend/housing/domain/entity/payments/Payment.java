package com.backend.housing.domain.entity.payments;

import com.backend.housing.domain.entity.payments.enums.PaymentMethod;
import com.backend.housing.domain.entity.payments.enums.PaymentReferenceType;
import com.backend.housing.domain.entity.payments.enums.PaymentStatus;
import com.backend.housing.domain.entity.payments.valueobjects.PaymentId;
import com.backend.housing.domain.exceptions.InvalidPaymentStateException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Payment {

    private final PaymentId id;
    private final UUID referenceId;
    private final PaymentReferenceType referenceType;
    private final BigDecimal amount;
    private final String currency;
    private PaymentStatus status;
    private PaymentMethod method;
    private final LocalDateTime createdAt;
    private LocalDateTime paidAt;

    private String checkoutSessionId;
    private String checkoutUrl;

    private Payment(PaymentId id,
                    UUID referenceId,
                    PaymentReferenceType referenceType,
                    BigDecimal amount,
                    String currency,
                    PaymentMethod method,
                    String checkoutSessionId,
                    String checkoutUrl) {
        this.id = id;
        this.referenceId = referenceId;
        this.referenceType = referenceType;
        this.amount = amount;
        this.currency = currency;
        this.method = method;
        this.checkoutSessionId = checkoutSessionId;
        this.checkoutUrl = checkoutUrl;
        this.status = PaymentStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public static Payment createWithCheckoutSession(UUID referenceId,
                                                    PaymentReferenceType referenceType,
                                                    BigDecimal amount,
                                                    String currency,
                                                    PaymentMethod method,
                                                    String checkoutSessionId,
                                                    String checkoutUrl) {

        return new Payment(
                PaymentId.generate(),
                referenceId,
                referenceType,
                amount,
                currency,
                method,
                checkoutSessionId,
                checkoutUrl
        );
    }

    private Payment(PaymentId id,
                    UUID referenceId,
                    PaymentReferenceType referenceType,
                    BigDecimal amount,
                    String currency,
                    PaymentStatus status,
                    PaymentMethod method,
                    LocalDateTime createdAt,
                    LocalDateTime paidAt,
                    String checkoutSessionId,
                    String checkoutUrl) {

        this.id = id;
        this.referenceId = referenceId;
        this.referenceType = referenceType;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.method = method;
        this.createdAt = createdAt;
        this.paidAt = paidAt;
        this.checkoutSessionId = checkoutSessionId;
        this.checkoutUrl = checkoutUrl;
    }

    public static Payment reconstitute(
            PaymentId id,
            UUID referenceId,
            PaymentReferenceType referenceType,
            BigDecimal amount,
            String currency,
            PaymentStatus paymentStatus,
            PaymentMethod method,
            LocalDateTime createdAt,
            LocalDateTime paidAt,
            String checkoutSessionId,
            String checkoutUrl

    ){
        return new Payment(
                id,
                referenceId,
                referenceType,
                amount,
                currency,
                paymentStatus,
                method,
                createdAt,
                paidAt,
                checkoutSessionId,
                checkoutUrl
        );
    }

    public void markAsSucceeded() {
        if (this.status == PaymentStatus.SUCCEEDED) {
            return;
        }

        if (this.status != PaymentStatus.PENDING) {
            throw new InvalidPaymentStateException(
                    "Payment must be PENDING to succeed. Current status: " + this.status
            );
        }

        this.status = PaymentStatus.SUCCEEDED;
        this.paidAt = LocalDateTime.now();
    }

    public void markAsFailed() {
        if (this.status == PaymentStatus.SUCCEEDED) {
            throw new InvalidPaymentStateException(
                    "Cannot mark a SUCCEEDED payment as FAILED"
            );
        }
        this.status = PaymentStatus.FAILED;
    }

    public PaymentId getId() { return id; }
    public UUID getReferenceId() { return referenceId; }
    public PaymentReferenceType getReferenceType() { return referenceType; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public PaymentStatus getStatus() { return status; }
    public PaymentMethod getMethod() { return method; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getPaidAt() { return paidAt; }
    public String getCheckoutSessionId() { return checkoutSessionId; }
    public String getCheckoutUrl() { return checkoutUrl; }
}
package com.backend.housing.application.commands.payments;

import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Getter
public class HandlePaymentWebhookCommand {

    private final String checkoutSessionId;
    private final UUID referenceId;
    private final String referenceType;
    private final LocalDate paymentConfirmedDate;

    private HandlePaymentWebhookCommand(Builder builder) {
        this.checkoutSessionId = Objects.requireNonNull(builder.checkoutSessionId, "checkoutSessionId is required");
        this.referenceId = Objects.requireNonNull(builder.referenceId, "referenceId is required");
        this.referenceType = Objects.requireNonNull(builder.referenceType, "referenceType is required");
        this.paymentConfirmedDate = Objects.requireNonNull(builder.paymentConfirmedDate, "paymentConfirmedDate is required");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String checkoutSessionId;
        private UUID referenceId;
        private String referenceType;
        private LocalDate paymentConfirmedDate;

        public Builder checkoutSessionId(String checkoutSessionId) {
            this.checkoutSessionId = checkoutSessionId;
            return this;
        }

        public Builder referenceId(UUID referenceId) {
            this.referenceId = referenceId;
            return this;
        }

        public Builder referenceType(String referenceType) {
            this.referenceType = referenceType;
            return this;
        }

        public Builder paymentConfirmedDate(LocalDate paymentConfirmedDate) {
            this.paymentConfirmedDate = paymentConfirmedDate;
            return this;
        }

        public HandlePaymentWebhookCommand build() {
            return new HandlePaymentWebhookCommand(this);
        }
    }
}
package com.backend.housing.application.commands.payments;

import com.backend.housing.domain.entity.payments.valueobjects.PaymentId;
import lombok.Getter;

import java.util.Objects;

@Getter
public class ConfirmPaymentCommand {

    private final PaymentId paymentId;

    private ConfirmPaymentCommand(Builder builder) {
        this.paymentId = Objects.requireNonNull(builder.paymentId, "PaymentId is required");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private PaymentId paymentId;

        public Builder paymentId(PaymentId paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public ConfirmPaymentCommand build() {
            return new ConfirmPaymentCommand(this);
        }
    }
}
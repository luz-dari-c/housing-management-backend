package com.backend.housing.domain.entity.payments.valueobjects;

import java.util.Objects;
import java.util.UUID;

public class PaymentId {

    private final UUID value;

    private PaymentId(UUID value) {
        this.value = value;
    }

    public static PaymentId of(UUID value) {
        return new PaymentId(value);
    }

    public static PaymentId generate() {
        return new PaymentId(UUID.randomUUID());
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentId)) return false;
        PaymentId that = (PaymentId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
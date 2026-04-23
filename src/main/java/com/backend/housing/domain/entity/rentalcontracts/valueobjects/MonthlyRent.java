package com.backend.housing.domain.entity.rentalcontracts.valueobjects;

import java.math.BigDecimal;
import java.util.Objects;

public class MonthlyRent {
    private final BigDecimal amount;

    private MonthlyRent(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Monthly rent cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Monthly rent must be greater than zero");
        }
        this.amount = amount;
    }

    public static MonthlyRent of(BigDecimal amount) {
        return new MonthlyRent(amount);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthlyRent that = (MonthlyRent) o;
        return Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public String toString() {
        return "$" + amount;
    }
}
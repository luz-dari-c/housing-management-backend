package com.backend.housing.domain.entity.rentalcontracts.valueobjects;

import java.math.BigDecimal;
import java.util.Objects;

public class PeriodRent {
    private final BigDecimal amount;

    private PeriodRent(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("El valor del período no puede ser nulo");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El valor del período debe ser mayor que cero");
        }
        this.amount = amount;
    }

    public static PeriodRent of(BigDecimal amount) {
        return new PeriodRent(amount);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PeriodRent that = (PeriodRent) o;
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
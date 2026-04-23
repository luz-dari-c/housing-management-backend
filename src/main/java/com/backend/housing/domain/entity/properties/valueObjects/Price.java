package com.backend.housing.domain.entity.properties.valueObjects;

import com.backend.housing.domain.entity.properties.enums.TransactionType;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {
    private final BigDecimal amount;
    private final TransactionType transactionType;

    private Price(BigDecimal amount, TransactionType transactionType) {
        this.transactionType = Objects.requireNonNull(transactionType, "Transaction type cannot be null");

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

        this.amount = amount;
    }

    public static Price forSale(BigDecimal amount) {
        return new Price(amount, TransactionType.SALE);
    }

    public static Price forRent(BigDecimal amount) {
        return new Price(amount, TransactionType.RENT);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public boolean isForSale() {
        return transactionType == TransactionType.SALE;
    }

    public boolean isForRent() {
        return transactionType == TransactionType.RENT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(amount, price.amount) &&
                transactionType == price.transactionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, transactionType);
    }

    @Override
    public String toString() {
        return "Price{" +
                "amount=" + amount +
                ", transactionType=" + transactionType +
                '}';
    }
}
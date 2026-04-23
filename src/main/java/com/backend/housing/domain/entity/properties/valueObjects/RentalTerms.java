package com.backend.housing.domain.entity.properties.valueObjects;

import com.backend.housing.domain.entity.properties.enums.PaymentFrequency;

import java.util.Objects;

public class RentalTerms {

    private final PaymentFrequency paymentFrequency;
    private final boolean petsAllowed;
    private final boolean furnished;

    public RentalTerms(PaymentFrequency paymentFrequency, boolean petsAllowed, boolean furnished) {
        this.paymentFrequency = Objects.requireNonNull(paymentFrequency, "Payment frequency is required");
        this.petsAllowed = petsAllowed;
        this.furnished = furnished;
    }

    public PaymentFrequency getPaymentFrequency() {
        return paymentFrequency;
    }

    public boolean isPetsAllowed() {
        return petsAllowed;
    }

    public boolean isFurnished() {
        return furnished;
    }
}
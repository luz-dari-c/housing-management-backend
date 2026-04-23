package com.backend.housing.domain.exceptions;

public class PaymentNotFoundException extends DomainException {

    public PaymentNotFoundException() {
        super("Payment not found");
    }
}
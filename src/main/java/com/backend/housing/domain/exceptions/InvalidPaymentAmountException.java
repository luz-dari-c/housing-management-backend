package com.backend.housing.domain.exceptions;

public class InvalidPaymentAmountException extends DomainException {

    public InvalidPaymentAmountException() {
        super("Payment amount must be greater than zero");
    }
}
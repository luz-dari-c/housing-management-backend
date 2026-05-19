package com.backend.housing.application.dto.response.payments;

public record PaymentInitiateResponse(
        String checkoutUrl,
        String message,
        String nextStep
) {}
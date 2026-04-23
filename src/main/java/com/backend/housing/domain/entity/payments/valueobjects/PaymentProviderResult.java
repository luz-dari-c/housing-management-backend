package com.backend.housing.domain.entity.payments.valueobjects;


public class PaymentProviderResult {

    private final String providerPaymentId;
    private final String clientSecret;

    public PaymentProviderResult(String providerPaymentId, String clientSecret) {
        this.providerPaymentId = providerPaymentId;
        this.clientSecret = clientSecret;
    }

    public String getProviderPaymentId() {
        return providerPaymentId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
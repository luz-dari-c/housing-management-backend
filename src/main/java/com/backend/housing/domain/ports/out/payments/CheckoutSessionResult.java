package com.backend.housing.domain.ports.out.payments;

public class CheckoutSessionResult {

    private final String sessionId;
    private final String checkoutUrl;

    public CheckoutSessionResult(String sessionId, String checkoutUrl) {
        this.sessionId = sessionId;
        this.checkoutUrl = checkoutUrl;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getCheckoutUrl() {
        return checkoutUrl;
    }
}
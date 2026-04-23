package com.backend.housing.infrastructure.external.payments;

import com.backend.housing.domain.entity.payments.enums.PaymentReferenceType;
import com.backend.housing.domain.ports.out.payments.CheckoutSessionResult;
import com.backend.housing.domain.ports.out.payments.PaymentProviderPort;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class StripeCheckoutAdapter implements PaymentProviderPort {

    @Value("${stripe.success-url}")
    private String defaultSuccessUrl;

    @Value("${stripe.cancel-url}")
    private String defaultCancelUrl;

    @Override
    public CheckoutSessionResult createCheckoutSession(BigDecimal amount,
                                                       String currency,
                                                       UUID referenceId,
                                                       PaymentReferenceType referenceType,
                                                       String successUrl,
                                                       String cancelUrl) {
        try {

            long amountInCents = amount
                    .multiply(new BigDecimal("100"))
                    .longValue();

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(successUrl != null ? successUrl : defaultSuccessUrl)
                    .setCancelUrl(cancelUrl != null ? cancelUrl : defaultCancelUrl)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency(currency.toLowerCase())
                                                    .setUnitAmount(amountInCents)
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName(getProductName(referenceType))
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .putMetadata("referenceId", referenceId.toString())
                    .putMetadata("referenceType", referenceType.name())
                    .build();

            Session session = Session.create(params);

            return new CheckoutSessionResult(
                    session.getId(),
                    session.getUrl()
            );

        } catch (StripeException e) {
            throw new RuntimeException("Error creating Stripe checkout session: " + e.getMessage(), e);
        }
    }

    private String getProductName(PaymentReferenceType referenceType) {
        if (referenceType == PaymentReferenceType.RENTAL) {
            return "Rental Contract Payment";
        }
        return "Property Payment";
    }
}
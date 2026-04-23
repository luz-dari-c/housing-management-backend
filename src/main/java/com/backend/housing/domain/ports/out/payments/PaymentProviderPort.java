package com.backend.housing.domain.ports.out.payments;

import com.backend.housing.domain.entity.payments.Payment;
import com.backend.housing.domain.entity.payments.enums.PaymentReferenceType;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;


public interface PaymentProviderPort {


    CheckoutSessionResult createCheckoutSession(BigDecimal amount,
                                                String currency,
                                                UUID referenceId,
                                                PaymentReferenceType referenceType,
                                                String successUrl,
                                                String cancelUrl);



}
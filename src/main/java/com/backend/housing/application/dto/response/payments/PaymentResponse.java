package com.backend.housing.application.dto.response.payments;


import com.backend.housing.domain.entity.payments.enums.PaymentReferenceType;
import com.backend.housing.domain.entity.payments.enums.PaymentStatus;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


public record PaymentResponse (

     String paymentId,
     UUID referenceId,
     PaymentReferenceType referenceType,
     BigDecimal amount,
     String currency,
     PaymentStatus status,
     String providerPaymentId,
     String clientSecret,
     LocalDateTime createdAt)
{
}

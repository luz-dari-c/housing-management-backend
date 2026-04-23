package com.backend.housing.application.dto.response.payments;


import com.backend.housing.domain.entity.payments.enums.PaymentReferenceType;
import com.backend.housing.domain.entity.payments.enums.PaymentStatus;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class PaymentResponse {

    private String paymentId;
    private UUID referenceId;
    private PaymentReferenceType referenceType;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private String providerPaymentId;
    private String clientSecret;
    private LocalDateTime createdAt;

}

package com.backend.housing.application.dto.request.payments;

import com.backend.housing.domain.entity.payments.enums.PaymentMethod;
import com.backend.housing.domain.entity.payments.enums.PaymentReferenceType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter

public class CreatePaymentRequest {
    private UUID referenceId;
    private PaymentReferenceType referenceType;
    private BigDecimal amount;
    private String currency;
    private  PaymentMethod method;

}

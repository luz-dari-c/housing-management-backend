package com.backend.housing.application.dto.response.payments;

import com.backend.housing.domain.entity.payments.enums.PaymentMethod;
import com.backend.housing.domain.entity.payments.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentReceiptResponse(
        UUID paymentId,
        UUID contractId,
        BigDecimal amount,
        String currency,
        PaymentStatus status,
        PaymentMethod method,
        LocalDateTime createdAt,
        LocalDateTime paidAt,
        String checkoutUrl
) {}
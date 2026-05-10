package com.backend.housing.application.dto.response.payments;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentHistoryItem(
        String paymentId,
        BigDecimal amount,
        String status,
        LocalDateTime paidAt,
        String period
) {
}
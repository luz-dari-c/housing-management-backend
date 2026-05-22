package com.backend.housing.application.dto.response.payments;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PaymentHistoryResponse(
        String contractId,
        String propertyTitle,
        BigDecimal periodRent,
        LocalDate nextPaymentDueDate,
        List<PaymentHistoryItem> payments
) {
}
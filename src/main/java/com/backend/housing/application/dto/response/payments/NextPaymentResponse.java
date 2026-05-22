package com.backend.housing.application.dto.response.payments;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record NextPaymentResponse(
        UUID contractId,
        String propertyTitle,
        BigDecimal amount,
        LocalDate dueDate,
        int daysRemaining,
        String paymentFrequency,
        boolean isOverdue,
        boolean canPayNextPeriod,
        String nextPeriodDescription,
        BigDecimal nextPeriodAmount
) {}
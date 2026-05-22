package com.backend.housing.application.dto.response.rentalcontracts;

import java.time.LocalDate;
import java.util.UUID;

public record CancellationStatusResponse(
        UUID contractId,
        String status,
        LocalDate effectiveDate,
        long daysRemaining,
        String cancelledBy,
        String message
) {}
package com.backend.housing.application.dto.response.properties;

import com.backend.housing.domain.entity.properties.enums.PropertyStatus;
import com.backend.housing.domain.entity.properties.enums.TransactionType;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PropertySummaryResponse(
        UUID id,
        String title,
        String description,
        TransactionType transactionType,
        BigDecimal priceAmount,
        List<String> imageUrls,
        PropertyStatus status

        ) {}
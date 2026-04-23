package com.backend.housing.application.dto.response.properties;

import com.backend.housing.domain.entity.properties.enums.TransactionType;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
public class PropertySummaryResponse {

    private UUID id;
    private String title;
    private String description;
    private TransactionType transactionType;
    private BigDecimal priceAmount;
    private List<String> imageUrls;

    public PropertySummaryResponse(UUID id, String title, String description,
                                   TransactionType transactionType, BigDecimal priceAmount,
                                   List<String> imageUrls) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.transactionType = transactionType;
        this.priceAmount = priceAmount;
        this.imageUrls = imageUrls;
    }
}
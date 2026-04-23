package com.backend.housing.application.dto.response.properties;

import com.backend.housing.domain.entity.properties.enums.PropertyStatus;
import com.backend.housing.domain.entity.properties.enums.TransactionType;
import com.backend.housing.domain.entity.properties.enums.TypeProperty;
import com.backend.housing.domain.entity.properties.valueObjects.Address;
import com.backend.housing.domain.entity.properties.valueObjects.Coordinates;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class CreatePropertyResponse {
    private final UUID id;
    private final String title;
    private final String description;
    private final Coordinates coordinates;
    private final TransactionType transactionType;
    private final BigDecimal priceAmount;
    private final TypeProperty typeProperty;
    private final PropertyStatus status;
    private final Long ownerId;
    private final LocalDateTime createdAt;
    private final List<String> imageUrls;
    private final Address address;

    public CreatePropertyResponse(UUID id, String title, String description,
                                  Coordinates coordinates, TransactionType transactionType,
                                  BigDecimal priceAmount, TypeProperty typeProperty,
                                  PropertyStatus status, Long ownerId,
                                  LocalDateTime createdAt, List<String> imageUrls,
                                  Address address) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.coordinates = coordinates;
        this.transactionType = transactionType;
        this.priceAmount = priceAmount;
        this.typeProperty = typeProperty;
        this.status = status;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.imageUrls = imageUrls;
        this.address = address;
    }
}
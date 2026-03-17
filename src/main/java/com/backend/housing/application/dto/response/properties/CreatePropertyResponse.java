package com.backend.housing.application.dto.response.properties;

import com.backend.housing.domain.entity.properties.PropertyStatus;
import com.backend.housing.domain.entity.properties.TypeProperty;
import com.backend.housing.domain.entity.properties.valueObjects.Address;
import com.backend.housing.domain.entity.properties.valueObjects.Coordinates;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CreatePropertyResponse {
    private final Long id;
    private final String title;
    private final String description;
    private final Coordinates coordinates;
    private final BigDecimal salePrice;
    private final BigDecimal rentPrice;
    private final TypeProperty typeProperty;
    private final PropertyStatus status;
    private final Long ownerId;
    private final LocalDateTime createdAt;
    private final List<String> imageUrls;
    private final Address address;

    public CreatePropertyResponse(Long id, String title, String description,
                                  Coordinates coordinates, BigDecimal salePrice,
                                  BigDecimal rentPrice, TypeProperty typeProperty,
                                  PropertyStatus status, Long ownerId,
                                  LocalDateTime createdAt, LocalDateTime updatedAt,
                                  List<String> imageUrls,
                                  Address address) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.coordinates = coordinates;
        this.salePrice = salePrice;
        this.rentPrice = rentPrice;
        this.typeProperty = typeProperty;
        this.status = status;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.imageUrls = imageUrls;
        this.address = address;
    }


}
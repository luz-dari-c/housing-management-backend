package com.backend.housing.application.dto.response.properties;

import com.backend.housing.domain.entity.properties.enums.PropertyStatus;
import com.backend.housing.domain.entity.properties.enums.TransactionType;
import com.backend.housing.domain.entity.properties.enums.TypeProperty;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class UpdatePropertyResponse {

    private final UUID id;
    private final String title;
    private final String description;
    private final TransactionType transactionType;
    private final BigDecimal priceAmount;
    private final TypeProperty typeProperty;
    private final PropertyStatus status;
    private final Integer numberOfBedrooms;
    private final Integer numberOfBathrooms;
    private final Integer areaInSquareMeters;
    private final Boolean petsAllowed;
    private final Boolean furnished;
    private final List<String> imageUrls;
    private final String street;
    private final String city;
    private final String state;
    private final String country;
    private final String postalCode;
    private final BigDecimal latitude;
    private final BigDecimal longitude;
    private final LocalDateTime updatedAt;

    public UpdatePropertyResponse(UUID id, String title, String description,
                                  TransactionType transactionType, BigDecimal priceAmount,
                                  TypeProperty typeProperty, PropertyStatus status,
                                  Integer numberOfBedrooms, Integer numberOfBathrooms,
                                  Integer areaInSquareMeters, Boolean petsAllowed,
                                  Boolean furnished, List<String> imageUrls,
                                  String street, String city, String state,
                                  String country, String postalCode,
                                  BigDecimal latitude, BigDecimal longitude,
                                  LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.transactionType = transactionType;
        this.priceAmount = priceAmount;
        this.typeProperty = typeProperty;
        this.status = status;
        this.numberOfBedrooms = numberOfBedrooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.areaInSquareMeters = areaInSquareMeters;
        this.petsAllowed = petsAllowed;
        this.furnished = furnished;
        this.imageUrls = imageUrls;
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.updatedAt = updatedAt;
    }
}
package com.backend.housing.application.dto.response.properties;

import com.backend.housing.domain.entity.properties.enums.PropertyStatus;
import com.backend.housing.domain.entity.properties.enums.TransactionType;
import com.backend.housing.domain.entity.properties.enums.TypeProperty;
import com.backend.housing.domain.entity.properties.valueObjects.Address;
import com.backend.housing.domain.entity.properties.valueObjects.Coordinates;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PropertyDetailResponse {

    private UUID id;
    private String title;
    private String description;
    private Coordinates coordinates;
    private TransactionType transactionType;
    private BigDecimal priceAmount;
    private TypeProperty typeProperty;
    private PropertyStatus status;
    private Long ownerId;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;
    private List<String> imageUrls;
    private Integer numberOfBedrooms;
    private Integer numberOfBathrooms;
    private Integer areaInSquareMeters;
    private boolean petsAllowed;
    private boolean furnished;
    private Address address;

    public PropertyDetailResponse(UUID id, String title, String description,
                                  Coordinates coordinates, TransactionType transactionType,
                                  BigDecimal priceAmount, TypeProperty typeProperty,
                                  PropertyStatus status, Long ownerId,
                                  LocalDateTime updatedAt, LocalDateTime publishedAt,
                                  List<String> imageUrls, Integer numberOfBedrooms,
                                  Integer numberOfBathrooms, Integer areaInSquareMeters,
                                  boolean petsAllowed, boolean furnished, Address address) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.coordinates = coordinates;
        this.transactionType = transactionType;
        this.priceAmount = priceAmount;
        this.typeProperty = typeProperty;
        this.status = status;
        this.ownerId = ownerId;
        this.updatedAt = updatedAt;
        this.publishedAt = publishedAt;
        this.imageUrls = imageUrls;
        this.numberOfBedrooms = numberOfBedrooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.areaInSquareMeters = areaInSquareMeters;
        this.petsAllowed = petsAllowed;
        this.furnished = furnished;
        this.address = address;
    }
}
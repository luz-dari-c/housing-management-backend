package com.backend.housing.domain.entity.properties;

import com.backend.housing.domain.entity.properties.valueObjects.Address;
import com.backend.housing.domain.entity.properties.valueObjects.Coordinates;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Property {

    private PropertyId id;
    private String title;
    private String description;
    private Coordinates coordinates;
    private BigDecimal salePrice;
    private BigDecimal rentPrice;
    private Modality modality;
    private PropertyStatus status;
    private Long ownerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;
    private List<String> imageUrls;
    private Integer numberOfBedrooms;
    private Integer numberOfBathrooms;
    private Integer areaInSquareMeters;
    private boolean petsAllowed;
    private boolean furnished;
    private Address address;

    public Property(LocalDateTime createdAt, Long ownerId, PropertyId id, String title, String description, Coordinates coordinates, BigDecimal salePrice, BigDecimal rentPrice, Modality modality, PropertyStatus status, LocalDateTime updatedAt, LocalDateTime publishedAt, List<String> imageUrls, Integer numberOfBedrooms, Integer numberOfBathrooms, Integer areaInSquareMeters, boolean petsAllowed, Address address, boolean furnished) {
        this.createdAt = createdAt;
        this.ownerId = ownerId;
        this.id = id;
        this.title = title;
        this.description = description;
        this.coordinates = coordinates;
        this.salePrice = salePrice;
        this.rentPrice = rentPrice;
        this.modality = modality;
        this.status = status;
        this.updatedAt = updatedAt;
        this.publishedAt = publishedAt;
        this.imageUrls = imageUrls;
        this.numberOfBedrooms = numberOfBedrooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.areaInSquareMeters = areaInSquareMeters;
        this.petsAllowed = petsAllowed;
        this.address = address;
        this.furnished = furnished;
    }
}

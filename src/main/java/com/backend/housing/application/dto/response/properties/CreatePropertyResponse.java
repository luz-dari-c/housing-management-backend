package com.backend.housing.application.dto.response.properties;

import com.backend.housing.domain.entity.properties.Modality;
import com.backend.housing.domain.entity.properties.PropertyStatus;
import com.backend.housing.domain.entity.properties.valueObjects.Address;
import com.backend.housing.domain.entity.properties.valueObjects.Coordinates;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CreatePropertyResponse {
    private final Long id;
    private final String title;
    private final String description;
    private final Coordinates coordinates;
    private final BigDecimal salePrice;
    private final BigDecimal rentPrice;
    private final Modality modality;
    private final PropertyStatus status;
    private final Long ownerId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime publishedAt;
    private final List<String> imageUrls;
    private final Integer numberOfBedrooms;
    private final Integer numberOfBathrooms;
    private final Integer areaInSquareMeters;
    private final boolean petsAllowed;
    private final boolean furnished;
    private final Address address;

    public CreatePropertyResponse(Long id, String title, String description,
                                  Coordinates coordinates, BigDecimal salePrice,
                                  BigDecimal rentPrice, Modality modality,
                                  PropertyStatus status, Long ownerId,
                                  LocalDateTime createdAt, LocalDateTime updatedAt,
                                  LocalDateTime publishedAt, List<String> imageUrls,
                                  Integer numberOfBedrooms, Integer numberOfBathrooms,
                                  Integer areaInSquareMeters, boolean petsAllowed,
                                  boolean furnished, Address address) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.coordinates = coordinates;
        this.salePrice = salePrice;
        this.rentPrice = rentPrice;
        this.modality = modality;
        this.status = status;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
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

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Coordinates getCoordinates() { return coordinates; }
    public BigDecimal getSalePrice() { return salePrice; }
    public BigDecimal getRentPrice() { return rentPrice; }
    public Modality getModality() { return modality; }
    public PropertyStatus getStatus() { return status; }
    public Long getOwnerId() { return ownerId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public LocalDateTime getPublishedAt() { return publishedAt; }
    public List<String> getImageUrls() { return imageUrls; }
    public Integer getNumberOfBedrooms() { return numberOfBedrooms; }
    public Integer getNumberOfBathrooms() { return numberOfBathrooms; }
    public Integer getAreaInSquareMeters() { return areaInSquareMeters; }
    public boolean isPetsAllowed() { return petsAllowed; }
    public boolean isFurnished() { return furnished; }
    public Address getAddress() { return address; }
}
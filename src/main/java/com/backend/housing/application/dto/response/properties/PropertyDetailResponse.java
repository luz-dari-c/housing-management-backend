package com.backend.housing.application.dto.response.properties;

import com.backend.housing.domain.entity.properties.PropertyStatus;
import com.backend.housing.domain.entity.properties.TypeProperty;
import com.backend.housing.domain.entity.properties.valueObjects.Address;
import com.backend.housing.domain.entity.properties.valueObjects.Coordinates;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PropertyDetailResponse {

    private  Long id;
    private  String title;
    private  String description;
    private  Coordinates coordinates;
    private  BigDecimal salePrice;
    private  BigDecimal rentPrice;
    private  TypeProperty typeProperty;
    private  PropertyStatus status;
    private  Long ownerId;
    private  LocalDateTime updatedAt;
    private  LocalDateTime publishedAt;
    private  List<String> imageUrls;
    private  Integer numberOfBedrooms;
    private  Integer numberOfBathrooms;
    private  Integer areaInSquareMeters;
    private  boolean petsAllowed;
    private  boolean furnished;
    private  Address address;

    public PropertyDetailResponse(String title, Long id, String description, Coordinates coordinates, BigDecimal salePrice, BigDecimal rentPrice, PropertyStatus status, TypeProperty typeProperty, Long ownerId, LocalDateTime updatedAt, LocalDateTime publishedAt, List<String> imageUrls, Integer numberOfBedrooms, Integer areaInSquareMeters, boolean petsAllowed, Integer numberOfBathrooms, boolean furnished, Address address) {
        this.title = title;
        this.id = id;
        this.description = description;
        this.coordinates = coordinates;
        this.salePrice = salePrice;
        this.rentPrice = rentPrice;
        this.status = status;
        this.typeProperty = typeProperty;
        this.ownerId = ownerId;
        this.updatedAt = updatedAt;
        this.publishedAt = publishedAt;
        this.imageUrls = imageUrls;
        this.numberOfBedrooms = numberOfBedrooms;
        this.areaInSquareMeters = areaInSquareMeters;
        this.petsAllowed = petsAllowed;
        this.numberOfBathrooms = numberOfBathrooms;
        this.furnished = furnished;
        this.address = address;
    }

    }

package com.backend.housing.application.Commands.properties;

import com.backend.housing.domain.entity.properties.Modality;
import com.backend.housing.domain.entity.properties.PropertyStatus;
import com.backend.housing.domain.entity.properties.valueObjects.Address;
import com.backend.housing.domain.entity.properties.valueObjects.Coordinates;
import java.math.BigDecimal;
import java.util.List;

public class CreatePropertyCommand {
    private final Long ownerId;
    private final String title;
    private final String description;
    private final Coordinates coordinates;
    private final BigDecimal salePrice;
    private final BigDecimal rentPrice;
    private final Modality modality;
    private final PropertyStatus initialStatus;
    private final List<String> imageUrls;
    private final Integer numberOfBedrooms;
    private final Integer numberOfBathrooms;
    private final Integer areaInSquareMeters;
    private final boolean petsAllowed;
    private final Address address;
    private final boolean furnished;

    public CreatePropertyCommand(Long ownerId, String title, String description,
                                 Coordinates coordinates, BigDecimal salePrice,
                                 BigDecimal rentPrice, Modality modality,
                                 PropertyStatus initialStatus, List<String> imageUrls,
                                 Integer numberOfBedrooms, Integer numberOfBathrooms,
                                 Integer areaInSquareMeters, boolean petsAllowed,
                                 Address address, boolean furnished) {

        if (ownerId == null) {
            throw new IllegalArgumentException("ownerId is required");
        }
        if (title == null){
            throw new IllegalArgumentException("tittle is required");
        }
        if (modality ==null){
            throw new IllegalArgumentException("modality is required");
        }
        if (address == null){
            throw new IllegalArgumentException("address is required");
        }

        if (salePrice == null && rentPrice == null) {
            throw new IllegalArgumentException("You need to enter a rental or sale amount");
        }

        if (salePrice != null && salePrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("salePrice must be greater than 0");
        }

        if (rentPrice != null && rentPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("rentPrice must be greater than 0");
        }


        this.initialStatus = initialStatus != null
                ? initialStatus
                : PropertyStatus.CREATED;

        this.ownerId = ownerId;
        this.title = title;
        this.description = description;
        this.coordinates = coordinates;
        this.salePrice = salePrice;
        this.rentPrice = rentPrice;
        this.modality = modality;
        this.imageUrls = imageUrls;
        this.numberOfBedrooms = numberOfBedrooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.areaInSquareMeters = areaInSquareMeters;
        this.petsAllowed = petsAllowed;
        this.address = address;
        this.furnished = furnished;
    }

    public Long getOwnerId() { return ownerId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Coordinates getCoordinates() { return coordinates; }
    public BigDecimal getSalePrice() { return salePrice; }
    public BigDecimal getRentPrice() { return rentPrice; }
    public Modality getModality() { return modality; }
    public PropertyStatus getInitialStatus() { return initialStatus; }
    public List<String> getImageUrls() { return imageUrls; }
    public Integer getNumberOfBedrooms() { return numberOfBedrooms; }
    public Integer getNumberOfBathrooms() { return numberOfBathrooms; }
    public Integer getAreaInSquareMeters() { return areaInSquareMeters; }
    public boolean isPetsAllowed() { return petsAllowed; }
    public Address getAddress() { return address; }
    public boolean isFurnished() { return furnished; }
}
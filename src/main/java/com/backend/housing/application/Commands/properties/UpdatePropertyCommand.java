package com.backend.housing.application.Commands.properties;

import com.backend.housing.domain.entity.properties.Modality;
import com.backend.housing.domain.entity.properties.PropertyStatus;
import com.backend.housing.domain.entity.properties.valueObjects.Address;
import com.backend.housing.domain.entity.properties.valueObjects.Coordinates;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;

import java.math.BigDecimal;
import java.util.List;

public class UpdatePropertyCommand {

    private final PropertyId id;
    private final String title;
    private final String description;
    private final Coordinates coordinates;
    private final BigDecimal salePrice;
    private final BigDecimal rentPrice;
    private final Modality modality;
    private final PropertyStatus status;
    private final List<String> imageUrls;
    private final Integer numberOfBedrooms;
    private final Integer numberOfBathrooms;
    private final Integer areaInSquareMeters;
    private final Boolean petsAllowed;
    private final Boolean furnished;
    private final Address address;

    public UpdatePropertyCommand(
            PropertyId id,
            String title,
            String description,
            Coordinates coordinates,
            BigDecimal salePrice,
            BigDecimal rentPrice,
            Modality modality,
            PropertyStatus status,
            List<String> imageUrls,
            Integer numberOfBedrooms,
            Integer numberOfBathrooms,
            Integer areaInSquareMeters,
            Boolean petsAllowed,
            Address address,
            Boolean furnished
    ) {

        if (id == null) {
            throw new IllegalArgumentException("El id de la propiedad no puede ser nulo");
        }

        if (salePrice != null && salePrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio de venta debe ser mayor a 0");
        }

        if (rentPrice != null && rentPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio de arriendo debe ser mayor a 0");
        }

        this.id = id;
        this.title = title;
        this.description = description;
        this.coordinates = coordinates;
        this.salePrice = salePrice;
        this.rentPrice = rentPrice;
        this.modality = modality;
        this.status = status;
        this.imageUrls = imageUrls;
        this.numberOfBedrooms = numberOfBedrooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.areaInSquareMeters = areaInSquareMeters;
        this.petsAllowed = petsAllowed;
        this.address = address;
        this.furnished = furnished;
    }

    public PropertyId getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Coordinates getCoordinates() { return coordinates; }
    public BigDecimal getSalePrice() { return salePrice; }
    public BigDecimal getRentPrice() { return rentPrice; }
    public Modality getModality() { return modality; }
    public PropertyStatus getStatus() { return status; }
    public List<String> getImageUrls() { return imageUrls; }
    public Integer getNumberOfBedrooms() { return numberOfBedrooms; }
    public Integer getNumberOfBathrooms() { return numberOfBathrooms; }
    public Integer getAreaInSquareMeters() { return areaInSquareMeters; }
    public Boolean getPetsAllowed() { return petsAllowed; }
    public Boolean getFurnished() { return furnished; }
    public Address getAddress() { return address; }
}

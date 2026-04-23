package com.backend.housing.application.commands.properties;

import com.backend.housing.domain.entity.properties.enums.TransactionType;
import com.backend.housing.domain.entity.properties.enums.TypeProperty;
import com.backend.housing.domain.entity.properties.enums.PropertyStatus;
import com.backend.housing.domain.entity.properties.valueObjects.Address;
import com.backend.housing.domain.entity.properties.valueObjects.Coordinates;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class UpdatePropertyCommand {

    private final PropertyId id;
    private final String title;
    private final String description;
    private final Coordinates coordinates;
    private final TransactionType transactionType;
    private final BigDecimal priceAmount;
    private final TypeProperty typeProperty;
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
            TransactionType transactionType,
            BigDecimal priceAmount,
            TypeProperty typeProperty,
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
            throw new IllegalArgumentException("Property ID cannot be null");
        }

        if (priceAmount != null && priceAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

        this.id = id;
        this.title = title;
        this.description = description;
        this.coordinates = coordinates;
        this.transactionType = transactionType;
        this.priceAmount = priceAmount;
        this.typeProperty = typeProperty;
        this.status = status;
        this.imageUrls = imageUrls;
        this.numberOfBedrooms = numberOfBedrooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.areaInSquareMeters = areaInSquareMeters;
        this.petsAllowed = petsAllowed;
        this.address = address;
        this.furnished = furnished;
    }
}
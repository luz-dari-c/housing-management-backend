package com.backend.housing.application.commands.properties;

import com.backend.housing.domain.entity.properties.enums.PaymentFrequency;
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
public class CreatePropertyCommand {

    private final PropertyId propertyId;
    private final String title;
    private final String description;
    private final Coordinates coordinates;
    private final TransactionType transactionType;
    private final BigDecimal priceAmount;
    private final TypeProperty typeProperty;
    private final PropertyStatus initialStatus;
    private final List<String> imageUrls;
    private final Integer numberOfBedrooms;
    private final Integer numberOfBathrooms;
    private final Integer areaInSquareMeters;
    private final Boolean petsAllowed;
    private final Boolean furnished;
    private final PaymentFrequency paymentFrequency;
    private final Address address;

    public CreatePropertyCommand(PropertyId propertyId,
                                 String title,
                                 String description,
                                 Coordinates coordinates,
                                 TransactionType transactionType,
                                 BigDecimal priceAmount,
                                 TypeProperty typeProperty,
                                 PropertyStatus initialStatus,
                                 List<String> imageUrls,
                                 Integer numberOfBedrooms,
                                 Integer numberOfBathrooms,
                                 Integer areaInSquareMeters,
                                 Boolean petsAllowed,
                                 Boolean furnished,
                                 PaymentFrequency paymentFrequency,
                                 Address address) {

        if (propertyId == null) throw new IllegalArgumentException("propertyId is required");
        if (title == null || title.trim().isEmpty()) throw new IllegalArgumentException("title is required");
        if (typeProperty == null) throw new IllegalArgumentException("typeProperty is required");
        if (address == null) throw new IllegalArgumentException("address is required");
        if (transactionType == null) throw new IllegalArgumentException("transactionType is required");
        if (priceAmount == null || priceAmount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("priceAmount must be greater than zero");

        if (transactionType == TransactionType.RENT && paymentFrequency == null) {
            throw new IllegalArgumentException("paymentFrequency is required for rental properties");
        }

        this.propertyId = propertyId;
        this.title = title;
        this.description = description;
        this.coordinates = coordinates;
        this.transactionType = transactionType;
        this.priceAmount = priceAmount;
        this.typeProperty = typeProperty;
        this.initialStatus = initialStatus != null ? initialStatus : PropertyStatus.CREATED;
        this.imageUrls = imageUrls;
        this.numberOfBedrooms = numberOfBedrooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.areaInSquareMeters = areaInSquareMeters;
        this.petsAllowed = petsAllowed;
        this.furnished = furnished;
        this.paymentFrequency = paymentFrequency;
        this.address = address;
    }
}
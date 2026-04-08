package com.backend.housing.application.dto.request.properties;

import com.backend.housing.domain.entity.properties.enums.PaymentFrequency;
import com.backend.housing.domain.entity.properties.enums.TransactionType;
import com.backend.housing.domain.entity.properties.enums.TypeProperty;
import com.backend.housing.domain.entity.properties.valueObjects.Address;
import com.backend.housing.domain.entity.properties.valueObjects.Coordinates;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CreatePropertyRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String title;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @Valid
    @NotNull(message = "Address is required")
    private Address address;

    @Valid
    private Coordinates coordinates;

    @NotNull(message = "Transaction type is required (SALE or RENT)")
    private TransactionType transactionType;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    private BigDecimal priceAmount;

    @NotNull(message = "Property type is required")
    private TypeProperty typeProperty;

    private List<String> imageUrls;

    @Min(value = 0, message = "Number of bedrooms cannot be negative")
    private Integer numberOfBedrooms;

    @Min(value = 0, message = "Number of bathrooms cannot be negative")
    private Integer numberOfBathrooms;

    @Min(value = 1, message = "Area must be greater than zero if provided")
    private Integer areaInSquareMeters;

    private Boolean petsAllowed;

    private Boolean furnished;

    private PaymentFrequency paymentFrequency;
}
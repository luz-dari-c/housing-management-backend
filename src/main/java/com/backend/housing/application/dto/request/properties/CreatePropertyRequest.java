package com.backend.housing.application.dto.request.properties;

import com.backend.housing.domain.entity.properties.RentType;
import com.backend.housing.domain.entity.properties.TypeProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CreatePropertyRequest {

    @NotNull(message = "Owner ID is required")
    private Long ownerId;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String title;

    private String description;

    @Valid
    private CoordinatesRequest coordinates;

    @Valid
    private AddressRequest address;


    @DecimalMin(value = "0.0", inclusive = false, message = "Sale price must be greater than 0")
    private BigDecimal salePrice;

    @DecimalMin(value = "0.0", inclusive = false, message = "Rent price must be greater than 0")
    private BigDecimal rentPrice;

    @NotNull(message = "Modality is required")
    private TypeProperty typeProperty;

    private List<String> imageUrls;

    @Min(value = 0, message = "Number of bedrooms cannot be negative")
    private Integer numberOfBedrooms;

    @Min(value = 0, message = "Number of bathrooms cannot be negative")
    private Integer numberOfBathrooms;

    @Min(value = 1, message = "Area must be greater than 0")
    private Integer areaInSquareMeters;

    private boolean petsAllowed;

    private boolean furnished;

    private RentType rentType;

}
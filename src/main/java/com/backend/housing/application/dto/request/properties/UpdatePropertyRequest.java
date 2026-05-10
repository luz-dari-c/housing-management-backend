package com.backend.housing.application.dto.request.properties;


import com.backend.housing.domain.entity.properties.enums.TransactionType;
import com.backend.housing.domain.entity.properties.enums.TypeProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class UpdatePropertyRequest {

    @Size(min = 3, max = 200, message = "El título debe tener entre 3 y 200 caracteres")
    private String title;

    @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
    private String description;

    private TransactionType transactionType;

    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal priceAmount;

    private TypeProperty typeProperty;

    @Min(value = 0, message = "El número de habitaciones no puede ser negativo")
    private Integer numberOfBedrooms;

    @Min(value = 0, message = "El número de baños no puede ser negativo")
    private Integer numberOfBathrooms;

    @Min(value = 1, message = "El área debe ser mayor a 0")
    private Integer areaInSquareMeters;

    private Boolean petsAllowed;

    private Boolean furnished;

    private AddressRequest address;

    private CoordinatesRequest coordinates;

}

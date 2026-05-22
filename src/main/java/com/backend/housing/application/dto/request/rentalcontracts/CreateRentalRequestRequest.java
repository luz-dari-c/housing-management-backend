package com.backend.housing.application.dto.request.rentalcontracts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateRentalRequestRequest {

    @NotNull(message = "Propiedad ID es requerido")
    @NotBlank(message = "Propiedad ID no puede estar vacío")
    private String propertyId;

    @Positive(message = "El precio propuesto debe ser positivo")
    private BigDecimal proposedRent;

    @NotNull(message = "Fecha de inicio es requerida")
    private String startDate;

    @NotNull(message = "Duracion es requerida")
    @Positive(message = "Duracion debe ser un número positivo")
    private Integer duration;
}
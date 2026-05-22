package com.backend.housing.application.dto.request.properties;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CoordinatesRequest {
    @NotNull
    private BigDecimal latitud;

    @NotNull
    private BigDecimal longitud;


}

package com.backend.housing.infrastructure.persistence.entities;


import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class CoordinatesEmbeddable {

    private BigDecimal latitud;
    private BigDecimal longitud;

}

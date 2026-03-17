package com.backend.housing.infrastructure.persistence.entities.properties;


import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Embeddable
public class CoordinatesEmbeddable {

    private BigDecimal latitud;
    private BigDecimal longitud;

}

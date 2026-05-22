package com.backend.housing.infrastructure.persistence.entities.properties;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Embeddable
public class CoordinatesEmbeddable {

    @Column(precision = 38, scale = 8)
    private BigDecimal latitud;


    @Column(precision = 38, scale = 8)
    private BigDecimal longitud;

}

package com.backend.housing.domain.entity.properties.valueObjects;


import java.math.BigDecimal;

public class Coordinates {
    private BigDecimal latitud;
    private BigDecimal longitud;


    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }
}

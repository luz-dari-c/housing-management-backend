package com.backend.housing.domain.entity.properties.valueObjects;


import com.backend.housing.domain.exceptions.InvalidCoordinatesException;

import java.math.BigDecimal;

public class Coordinates {
    private BigDecimal latitud;
    private BigDecimal longitud;

    public Coordinates(BigDecimal longitud, BigDecimal latitud) {

        if ( latitud == null){
            throw new InvalidCoordinatesException("Latitud cannot be null");
        }
        if (longitud == null){
            throw new InvalidCoordinatesException("Longitud cannot be null");
        }


        this.longitud = longitud;
        this.latitud = latitud;
    }

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

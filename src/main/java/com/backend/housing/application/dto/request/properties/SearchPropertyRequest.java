package com.backend.housing.application.dto.request.properties;

import com.backend.housing.domain.entity.properties.RentType;
import com.backend.housing.domain.entity.properties.TypeProperty;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class SearchPropertyRequest {

    private String city;

    @Positive
    private BigDecimal minPrice;

    @Positive
    private BigDecimal maxPrice;

    private TypeProperty typeProperty;

    private RentType rentType;

    private Integer bedrooms;
    private Boolean petsAllowed;

    private Boolean furnished;


    public String getCity() {
        return city;
    }

    public @Positive BigDecimal getMinPrice() {
        return minPrice;
    }

    public @Positive BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public TypeProperty getTypeProperty() {
        return typeProperty;
    }

    public RentType getRentType() {
        return rentType;
    }

    public Integer getBedrooms() {
        return bedrooms;
    }

    public Boolean getPetsAllowed() {
        return petsAllowed;
    }

    public Boolean getFurnished() {
        return furnished;
    }
}
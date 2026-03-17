package com.backend.housing.domain.entity.properties.valueObjects;


import com.backend.housing.domain.entity.properties.RentType;
import com.backend.housing.domain.entity.properties.TypeProperty;

import java.math.BigDecimal;


public class SearchPropertyQuery {

    private String city;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private TypeProperty typeProperty;
    private RentType rentType;
    private Integer bedrooms;
    private Boolean petsAllowed;
    private Boolean furnished;

    public SearchPropertyQuery(
            String city,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            TypeProperty typeProperty,
            RentType rentType,
            Integer bedrooms,
            Boolean petsAllowed,
            Boolean furnished
    ) {
        this.city = city;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.typeProperty = typeProperty;
        this.rentType = rentType;
        this.bedrooms = bedrooms;
        this.petsAllowed = petsAllowed;
        this.furnished = furnished;
    }

    public String getCity() {
        return city;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public BigDecimal getMaxPrice() {
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



    public boolean hasCity() {
        return city != null && !city.isBlank();
    }

    public boolean hasMinPrice() {
        return minPrice != null;
    }

    public boolean hasMaxPrice() {
        return maxPrice != null;
    }

    public boolean hasTypeProperty() {
        return typeProperty != null;
    }

    public boolean hasRentType() {
        return rentType != null;
    }

    public boolean hasBedrooms() {
        return bedrooms != null;
    }

    public boolean hasPetsAllowed() {
        return petsAllowed != null;
    }

    public boolean hasFurnished() {
        return furnished != null;
    }
}

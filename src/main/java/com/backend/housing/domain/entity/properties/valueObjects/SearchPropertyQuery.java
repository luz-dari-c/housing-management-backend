package com.backend.housing.domain.entity.properties.valueObjects;


import com.backend.housing.domain.entity.properties.enums.RentModality;
import com.backend.housing.domain.entity.properties.enums.TypeProperty;

import java.math.BigDecimal;


public class SearchPropertyQuery {

    private String city;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private TypeProperty typeProperty;
    private Integer bedrooms;
    private Boolean petsAllowed;
    private Boolean furnished;

    public SearchPropertyQuery(
            String city,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            TypeProperty typeProperty,
            Integer bedrooms,
            Boolean petsAllowed,
            Boolean furnished
    ) {
        this.city = city;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.typeProperty = typeProperty;
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

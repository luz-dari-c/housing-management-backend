package com.backend.housing.application.dto.response.properties;


import com.backend.housing.domain.entity.properties.valueObjects.Address;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class PropertySummaryResponse {

    private long id;
    private  String title;
    private  String description;
    private BigDecimal salePrice;
    private  BigDecimal rentPrice;
    private List<String> imageUrls;

    public PropertySummaryResponse(long id, String title, String description, BigDecimal salePrice, BigDecimal rentPrice, List<String> imageUrls) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.salePrice = salePrice;
        this.rentPrice = rentPrice;
        this.imageUrls = imageUrls;
    }
}

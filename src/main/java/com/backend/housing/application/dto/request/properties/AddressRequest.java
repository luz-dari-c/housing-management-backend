package com.backend.housing.application.dto.request.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {

    @NotNull
    private String street;
    
    @NotNull
    private String city;
    
    @NotNull
    private String state;
    
    @NotNull
    private String country;
    
    @NotNull
    private String postalCode;
}

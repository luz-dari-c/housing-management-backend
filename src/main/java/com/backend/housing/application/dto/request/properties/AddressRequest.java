package com.backend.housing.application.dto.request.properties;


import jakarta.validation.constraints.NotNull;

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

    public @NotNull String getStreet() {
        return street;
    }

    public @NotNull String getCity() {
        return city;
    }

    public @NotNull String getCountry() {
        return country;
    }

    public @NotNull String getState() {
        return state;
    }

    public @NotNull String getPostalCode() {
        return postalCode;
    }
}

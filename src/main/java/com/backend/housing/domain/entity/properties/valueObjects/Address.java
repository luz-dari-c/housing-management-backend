package com.backend.housing.domain.entity.properties.valueObjects;

import com.backend.housing.domain.exceptions.InvalidAddressException;

public class Address {

    private String street;
    private String city;
    private String state;
    private String country;
    private String postalCode;

    public Address(String street, String state, String city, String country, String postalCode) {

        if (postalCode == null || postalCode.isBlank()){
           throw new InvalidAddressException("Postal code cannot be null");
        }

        if (street == null || street.isBlank()){
            throw new InvalidAddressException("Street cannot be null");
        }

        if (city == null || city.isBlank()){
            throw new InvalidAddressException("City cannot be null");
        }

        if (country == null || country.isBlank()){
            throw new InvalidAddressException("Country cannot be null");
        }

        if (state == null || state.isBlank()){
            throw new InvalidAddressException("State cannot be null");
        }

        this.street = street;
        this.state = state;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}

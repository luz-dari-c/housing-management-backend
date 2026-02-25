package com.backend.housing.infrastructure.persistence.entities;


import jakarta.persistence.Embeddable;

@Embeddable
public class AddressEmbeddable {

    private String street;
    private String city;
    private String state;
    private String country;
    private String postalCode;

}

package com.backend.housing.infrastructure.persistence.entities.properties;


import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Embeddable
public class AddressEmbeddable {

    private String street;
    private String city;
    private String state;
    private String country;
    private String postalCode;

}

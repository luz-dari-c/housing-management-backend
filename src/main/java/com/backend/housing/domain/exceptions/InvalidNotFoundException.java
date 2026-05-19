package com.backend.housing.domain.exceptions;

public class InvalidNotFoundException extends DomainException {

    public InvalidNotFoundException(String message) {
        super(message);
    }
}
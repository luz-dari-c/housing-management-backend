package com.backend.housing.domain.exceptions;


public class InvalidAddressException extends DomainException {

    public InvalidAddressException(String message){
        super(message);
    }
}

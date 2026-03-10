package com.backend.housing.domain.exceptions;


public abstract class DomainException extends RuntimeException {

    public DomainException(String message){
        super(message);
    }

}

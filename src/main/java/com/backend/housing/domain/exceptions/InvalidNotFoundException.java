package com.backend.housing.domain.exceptions;


public class InvalidNotFoundException extends RuntimeException {

    public InvalidNotFoundException(String message){
        super(message);
    }
}

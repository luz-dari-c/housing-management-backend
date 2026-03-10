package com.backend.housing.domain.exceptions;


public class InvalidCoordinatesException extends DomainException{

    public InvalidCoordinatesException(String message){
        super(message);
    }

}

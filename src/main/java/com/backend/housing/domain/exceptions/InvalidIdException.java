package com.backend.housing.domain.exceptions;


public class InvalidIdException  extends DomainException{

    public InvalidIdException(String message){
        super(message);
    }
}

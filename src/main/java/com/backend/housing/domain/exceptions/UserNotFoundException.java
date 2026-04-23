package com.backend.housing.domain.exceptions;


public class UserNotFoundException  extends DomainException{

    public UserNotFoundException(String message){
        super(message);
    }
}

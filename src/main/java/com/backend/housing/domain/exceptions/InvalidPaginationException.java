package com.backend.housing.domain.exceptions;


public class InvalidPaginationException  extends DomainException{

    public InvalidPaginationException(String message){
        super(message);
    }
}

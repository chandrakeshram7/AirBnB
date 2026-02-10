package com.cantech.projects.airBnbApp.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message ){
        super(message);
    }
}

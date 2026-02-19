package com.cantech.projects.airBnbApp.exceptions;

public class UnAuthorisedException extends RuntimeException{

    public UnAuthorisedException(String message){
        super(message);
    }
}

package com.cantech.projects.airBnbApp.advices;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class APIResponse <T>{

    private T data ;

    private APIError apiError;

    private LocalDateTime timestamp;

    public APIResponse(){
        this.timestamp = LocalDateTime.now();
    }

    public APIResponse(T data){
        this();
        this.data = data;
    }

    public APIResponse(APIError apiError){
        this();
        this.apiError = apiError;
    }

}

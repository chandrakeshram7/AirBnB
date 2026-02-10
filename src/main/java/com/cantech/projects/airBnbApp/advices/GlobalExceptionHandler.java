package com.cantech.projects.airBnbApp.advices;

import com.cantech.projects.airBnbApp.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException exception){
        APIError apiError = APIError.builder().message(exception.getMessage()).status(HttpStatus.NOT_FOUND).build();
        return new ResponseEntity<>(new APIResponse<>(apiError), HttpStatus.NOT_FOUND);
    }
}

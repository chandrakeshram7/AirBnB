package com.cantech.projects.airBnbApp.advices;

import com.cantech.projects.airBnbApp.exceptions.ResourceNotFoundException;
import com.cantech.projects.airBnbApp.exceptions.UnAuthorisedException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException exception){
        APIError apiError = APIError.builder().message(exception.getMessage()).status(HttpStatus.NOT_FOUND).build();
        return new ResponseEntity<>(new APIResponse<>(apiError), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnAuthorisedException.class)
    public ResponseEntity<?> handleUnAuthorisedException(UnAuthorisedException exception){
        APIError apiError = APIError.builder().message(exception.getMessage()).status(HttpStatus.UNAUTHORIZED).build();
        return new ResponseEntity<>(new APIResponse<>(apiError), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<?> handleJwtException(JwtException exception){
        APIError apiError = APIError.builder().message(exception.getMessage()).status(HttpStatus.BAD_REQUEST).build();
        return new ResponseEntity<>(new APIResponse<>(apiError), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException exception){
        APIError apiError = APIError.builder().message(exception.getMessage()).status(HttpStatus.FORBIDDEN).build();
        return new ResponseEntity<>(new APIResponse<>(apiError), HttpStatus.FORBIDDEN);
    }


}

package com.example.feedmicroservice.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomAccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleCustomAccessDeniedException(CustomAccessDeniedException ex) {
        String errorMessage = ex.getMessage();
        String customMessage = ex.getCustomMessage();

        // Create a custom JSON response
        String jsonResponse = "{\"error\": \"" + errorMessage + "\", \"customMessage\": \"" + customMessage + "\"}";
        return new ResponseEntity<>(jsonResponse, HttpStatus.FORBIDDEN);
    }

    // Add more exception handlers if needed
}

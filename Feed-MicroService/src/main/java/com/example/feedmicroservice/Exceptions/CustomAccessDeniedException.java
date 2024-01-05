package com.example.feedmicroservice.Exceptions;

import lombok.Getter;

import java.nio.file.AccessDeniedException;

@Getter
public class CustomAccessDeniedException extends AccessDeniedException {

    private final String customMessage;

    public CustomAccessDeniedException(String message, String customMessage) {
        super(message);
        this.customMessage = customMessage;
    }

}
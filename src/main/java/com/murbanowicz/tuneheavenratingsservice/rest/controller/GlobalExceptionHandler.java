package com.murbanowicz.tuneheavenratingsservice.rest.controller;

import com.murbanowicz.tuneheavenratingsservice.exception.RestArgumentsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RestArgumentsException.class)
    public ResponseEntity<String> handleRestArgumentsException(RestArgumentsException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }
}
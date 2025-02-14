package com.ricardosantana.spring.usermanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CustomObjectNotFoundException extends ResponseStatusException {

    public CustomObjectNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
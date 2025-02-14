package com.ricardosantana.spring.usermanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CustomDataIntegrityViolationException extends ResponseStatusException {

    public CustomDataIntegrityViolationException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}

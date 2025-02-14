package com.ricardosantana.spring.usermanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CustomIllegalArgumentException extends ResponseStatusException {

    public CustomIllegalArgumentException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}

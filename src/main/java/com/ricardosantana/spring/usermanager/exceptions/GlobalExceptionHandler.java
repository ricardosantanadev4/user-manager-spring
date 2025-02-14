package com.ricardosantana.spring.usermanager.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> handleValidationException(MethodArgumentNotValidException e,
            HttpServletRequest request) {
        ValidationError errors = new ValidationError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                "Erro na validação dos campos", request.getRequestURI());

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.addError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}

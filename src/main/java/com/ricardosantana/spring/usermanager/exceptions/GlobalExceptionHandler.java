package com.ricardosantana.spring.usermanager.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> handleValidationException(
            MethodArgumentNotValidException e, HttpServletRequest request) {
        ValidationError errors = new ValidationError(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(), "Erro na validação dos campos",
                request.getRequestURI());

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.addError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationError> handleConstraintViolationException(
            ConstraintViolationException e, HttpServletRequest request) {
        ValidationError errors = new ValidationError(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(), "Erro na validação dos parâmetros",
                request.getRequestURI());

        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            errors.addError(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
    
    /*
     * Esse handler irá capturar a exceção gerada quando o Spring não consegue
     * deserializar um JSON
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ValidationError> handleJsonParseException(
            HttpMessageNotReadableException e, HttpServletRequest request) {

        // Verifica se a exceção está relacionada ao campo 'role'
        if (e.getMessage().contains("Cannot deserialize value of type")) {
            ValidationError errors = new ValidationError(
                    LocalDateTime.now(),
                    HttpStatus.BAD_REQUEST.value(),
                    "Valor inválido para o campo 'role'. Os valores aceitos são: [USER, ADMIN].",
                    request.getRequestURI());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        // Caso o erro não seja relacionado ao campo 'role', mensagem genérica
        ValidationError errors = new ValidationError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Erro ao processar o JSON.",
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

}

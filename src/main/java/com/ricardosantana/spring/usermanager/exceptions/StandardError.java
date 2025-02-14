package com.ricardosantana.spring.usermanager.exceptions;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StandardError {

    @JsonFormat(pattern = "HH:mm - dd/MM/yyyy")
    private LocalDateTime timestamp;
    private Integer status;
    private String message;
    private String path;

    public StandardError(LocalDateTime timestamp, Integer status, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.path = path;
    }
}

package com.ufcg.booker.config;

import org.springframework.validation.FieldError;

public record FieldErrorOutputDTO(String field, String message) {

    public FieldErrorOutputDTO(FieldError fieldError) {
        this(fieldError.getField(), fieldError.getDefaultMessage());
    }
}

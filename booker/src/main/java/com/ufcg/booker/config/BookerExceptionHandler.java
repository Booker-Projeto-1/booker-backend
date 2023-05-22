package com.ufcg.booker.config;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestControllerAdvice
public class BookerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationErrorsOutputDTO handleValidationError(MethodArgumentNotValidException exception) {
        List<ObjectError> globalErrors = exception.getGlobalErrors();
        List<FieldError> fieldErrors = exception.getFieldErrors();

        return ValidationErrorsOutputDTO.from(globalErrors, fieldErrors);
    }
}

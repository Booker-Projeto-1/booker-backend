package com.ufcg.booker.config;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;

record ValidationErrorsOutputDTO(List<String> globalErrors, List<FieldErrorOutputDTO> fieldErrors) {
    static ValidationErrorsOutputDTO from(List<ObjectError> globalErrors, List<FieldError> fieldErrors) {
        List<String> globalErrorsMessages = globalErrors.stream().map(ObjectError::getDefaultMessage).toList();
        List<FieldErrorOutputDTO> fieldErrorsOutputDTO = fieldErrors.stream().map(FieldErrorOutputDTO::new).toList();

        return new ValidationErrorsOutputDTO(globalErrorsMessages, fieldErrorsOutputDTO);
    }
}

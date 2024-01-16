package ru.kudryashov.springcource.springSecurityApp.exceptions;

import org.springframework.validation.FieldError;

import java.util.List;

public class AuthValidatorException extends RuntimeException{
    private List<FieldError> listErrors;

    public AuthValidatorException(List<FieldError> listErrors) {
        this.listErrors = listErrors;
    }

    public List<FieldError> getListErrors() {
        return listErrors;
    }

    public void setListErrors(List<FieldError> listErrors) {
        this.listErrors = listErrors;
    }
}

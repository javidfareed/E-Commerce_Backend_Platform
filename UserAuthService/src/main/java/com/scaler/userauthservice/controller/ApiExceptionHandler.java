package com.scaler.userauthservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException e)
    {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,"Some fields are not valid");
        Map<String , String> errors = new HashMap<>();

        for(FieldError error : e.getBindingResult().getFieldErrors())
        {
            errors.put(error.getField() , error.getDefaultMessage());

        }

        problemDetail.setProperty("errors" , errors);

        return problemDetail;
    }
}

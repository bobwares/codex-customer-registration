package com.bobwares.customer.registration.controller;

import com.bobwares.customer.registration.exception.CustomerNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCustomerNotFound(CustomerNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Validation failed");
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        Map<String, String> validationErrors = new HashMap<>();
        for (FieldError fieldError : fieldErrors) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        body.put("details", validationErrors);
        return ResponseEntity.badRequest().body(body);
    }
}

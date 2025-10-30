/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.web
 * File: RestExceptionHandler.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-30T01:18:45Z
 * Exports: RestExceptionHandler
 * Description: Maps domain and validation exceptions to standardized HTTP error responses.
 */
package com.bobwares.customer.registration.web;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ProblemDetail> handleNotFound(EntityNotFoundException ex) {
    ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    detail.setTitle("Resource Not Found");
    detail.setDetail(ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detail);
  }

  @ExceptionHandler({IllegalArgumentException.class, ConstraintViolationException.class})
  public ResponseEntity<ProblemDetail> handleBadRequest(RuntimeException ex) {
    ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    detail.setTitle("Invalid Request");
    detail.setDetail(ex.getMessage());
    return ResponseEntity.badRequest().body(detail);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex) {
    ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
    detail.setTitle("Validation Failed");
    Map<String, String> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (a, b) -> a, HashMap::new));
    detail.setProperty("errors", errors);
    return ResponseEntity.unprocessableEntity().body(detail);
  }
}

/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.web
 * File: RestExceptionHandler.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Coding Agent
 * Date: 2025-10-29T16:56:41Z
 * Exports: RestExceptionHandler
 * Description: Translates application and validation exceptions into structured HTTP error responses.
 */
package com.bobwares.customer.registration.web;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ApiError> handleNotFound(EntityNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiError.from(HttpStatus.NOT_FOUND, ex.getMessage(), List.of()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ApiError.from(HttpStatus.BAD_REQUEST, ex.getMessage(), List.of()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
    List<String> details =
        ex.getBindingResult().getFieldErrors().stream()
            .map(RestExceptionHandler::formatFieldError)
            .toList();
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
        .body(ApiError.from(HttpStatus.UNPROCESSABLE_ENTITY, "Validation failed", details));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
    List<String> details = ex.getConstraintViolations().stream().map(v -> v.getPropertyPath() + ": " + v.getMessage()).toList();
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
        .body(ApiError.from(HttpStatus.UNPROCESSABLE_ENTITY, "Validation failed", details));
  }

  private static String formatFieldError(FieldError fieldError) {
    return fieldError.getField() + ": " + fieldError.getDefaultMessage();
  }

  public record ApiError(OffsetDateTime timestamp, int status, String error, String message, List<String> details) {

    static ApiError from(HttpStatus status, String message, List<String> details) {
      return new ApiError(OffsetDateTime.now(), status.value(), status.getReasonPhrase(), message, details);
    }
  }
}

/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.web
 * File: RestExceptionHandler.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares (bobwares@outlook.com)
 * Date: 2025-10-27T23:24:28Z
 * Exports: RestExceptionHandler
 * Description: Translates common service and validation exceptions into structured HTTP error responses.
 */
package com.bobwares.customer.registration.web;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleNotFound(
      EntityNotFoundException ex, HttpServletRequest request) {
    return build(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
  }

  @ExceptionHandler({IllegalArgumentException.class, ConstraintViolationException.class})
  public ResponseEntity<Map<String, Object>> handleBadRequest(
      Exception ex, HttpServletRequest request) {
    return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidation(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    String message =
        ex.getBindingResult().getFieldErrors().stream()
            .map(this::formatFieldError)
            .findFirst()
            .orElse("Validation failed");
    return build(HttpStatus.UNPROCESSABLE_ENTITY, message, request.getRequestURI());
  }

  private String formatFieldError(FieldError error) {
    return "%s %s".formatted(error.getField(), error.getDefaultMessage());
  }

  private ResponseEntity<Map<String, Object>> build(HttpStatus status, String message, String path) {
    Map<String, Object> body =
        Map.of(
            "timestamp", Instant.now(),
            "status", status.value(),
            "error", status.getReasonPhrase(),
            "message", message,
            "path", path);
    return ResponseEntity.status(status).body(body);
  }
}

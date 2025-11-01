package com.loblivious.spring6restmvc.controller;

import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomErrorController {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<List<Map<String, String>>> handleBindErrors(
      MethodArgumentNotValidException exception) {
    List<Map<String, String>> fieldErrors = exception.getBindingResult().getFieldErrors().stream()
        .map((fieldError) -> {
          Map<String, String> errorMap = new HashMap<>();
          errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
          return errorMap;
        })
        .toList();

    return ResponseEntity.badRequest().body(fieldErrors);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  ResponseEntity<List<Map<String, String>>> handleConstraintViolation(
      ConstraintViolationException exception) {
    List<Map<String, String>> errors = exception.getConstraintViolations().stream()
        .map(constraintViolation -> {
          Map<String, String> errorMap = new HashMap<>();
          errorMap.put(constraintViolation.getPropertyPath().toString(),
              constraintViolation.getMessage());
          return errorMap;
        }).toList();

    return ResponseEntity.badRequest().body(errors);
  }
}

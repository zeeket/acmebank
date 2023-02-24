package com.acmebank.customer.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;

import io.jsonwebtoken.ExpiredJwtException;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(CustomerAlreadyExistsException.class)
  public ResponseEntity<ErrorDTO> generateCustomerAlreadyExistsException(CustomerAlreadyExistsException ex) {
    ErrorDTO errorDTO = new ErrorDTO();
    errorDTO.setMessage(ex.getMessage());
    errorDTO.setStatus(String.valueOf(ex.getStatus().value()));
    errorDTO.setTime(new Date().toString());
    return new ResponseEntity<ErrorDTO>(errorDTO, ex.getStatus());
  }

  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<ErrorDTO> generateExpiredJwtException(ExpiredJwtException ex) {
    ErrorDTO errorDTO = new ErrorDTO();
    errorDTO.setMessage(ex.getMessage());
    errorDTO.setStatus(HttpStatus.UNAUTHORIZED.toString());
    errorDTO.setTime(new Date().toString());
    return new ResponseEntity<ErrorDTO>(errorDTO, HttpStatus.UNAUTHORIZED);
  }

}
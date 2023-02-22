package com.acmebank.customer.exception;

import org.springframework.http.HttpStatus;

public class CustomerAlreadyExistsException extends RuntimeException {

  public CustomerAlreadyExistsException(String message) {
    super(message);
  }

  public HttpStatus getStatus() {
    return HttpStatus.UNPROCESSABLE_ENTITY;
  }
}
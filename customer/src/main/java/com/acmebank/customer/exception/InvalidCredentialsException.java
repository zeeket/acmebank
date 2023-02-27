package com.acmebank.customer.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException  extends RuntimeException {

    public InvalidCredentialsException(String message){
        super(message);
      }
    
      public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
      }
}

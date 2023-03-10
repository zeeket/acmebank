package com.acmebank.customer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acmebank.customer.auth.AuthenticationService;
import com.acmebank.customer.auth.ConfirmPasswordRequest;
import com.acmebank.customer.auth.ConfirmPasswordResponse;
import com.acmebank.customer.auth.IAuthenticationFacade;
import com.acmebank.customer.customer.Customer;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

  @Autowired
  private AuthenticationService myAuthService;

  @GetMapping
  public ResponseEntity<Customer> getCustomer() {
    Customer customer = authenticationFacade.getLoggedInCustomer();
    customer.setPassword("********");
    return ResponseEntity.ok(customer);
  }

  @Autowired
  private IAuthenticationFacade authenticationFacade;

  @PostMapping(value = "/confirmpassword")
  public ResponseEntity<ConfirmPasswordResponse> confirmPassword(@RequestBody ConfirmPasswordRequest request) {
    var customerEmail =  authenticationFacade.getAuthentication().getName();
    var correct = myAuthService.checkIfValidOldPassword(customerEmail, request.getPassword());
    return ResponseEntity.ok(new ConfirmPasswordResponse(correct, customerEmail));
  }

}

package com.acmebank.customer.auth;

import org.springframework.security.core.Authentication;

import com.acmebank.customer.customer.Customer;

public interface IAuthenticationFacade {
    Authentication getAuthentication();
    Customer getLoggedInCustomer();
}
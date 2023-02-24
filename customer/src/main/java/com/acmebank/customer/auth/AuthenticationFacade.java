package com.acmebank.customer.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.acmebank.customer.customer.Customer;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

   @Override
   public Customer getLoggedInCustomer() {
        return (Customer) getAuthentication().getPrincipal();
   } 

}

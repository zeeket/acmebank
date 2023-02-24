package com.acmebank.customer.auth;

public class ConfirmPasswordRequest {

    private String password;

    public ConfirmPasswordRequest(final String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password){
        this.password = password;
    }

    public ConfirmPasswordRequest(){
    }
    
}

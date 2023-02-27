package com.acmebank.customer.auth;

public class ConfirmPasswordResponse {
    
    private boolean correct;
    private String customerEmail;

    public ConfirmPasswordResponse(){
    }

    public ConfirmPasswordResponse(final boolean correct, final String customerEmail) {
        this.correct = correct;
        this.customerEmail = customerEmail;
    }

    public boolean getCorrect() {
        return this.correct;
    }

    public void setCorrect(final boolean correct){
        this.correct = correct;
    }

    public String getCustomerEmail() {
        return this.customerEmail;
    }

    public void setCustomerEmail(final String customerEmail){
        this.customerEmail = customerEmail;
    }
    
}

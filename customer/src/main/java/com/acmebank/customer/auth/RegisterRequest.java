package com.acmebank.customer.auth;

public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;

    public RegisterRequest(final String firstname, final String lastname, final String email, final String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public RegisterRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(final String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastName(final String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password){
        this.password = password;
    }

    public RegisterRequest(){
    }
}

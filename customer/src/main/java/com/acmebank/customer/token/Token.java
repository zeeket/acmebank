package com.acmebank.customer.token;

import com.acmebank.customer.customer.Customer;

import jakarta.persistence.*;

@Entity
@Table(name = "token")
public class Token {

    public Token() {
    }

    public Token(String token, Customer customer) {
        this.token = token;
        this.customer = customer;
    }

    public boolean isExpired() {
        return this.expired;
    }

    public boolean isRevoked() {
        return this.revoked;
    }

    public void revoke() {
        this.revoked = true;
    }

    public void expire() {
        this.expired = true;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Id
    @GeneratedValue
    public long id;

    @Column(unique = true)
    public String token;

    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;

    public boolean revoked = false;

    public boolean expired = false;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    public Customer customer;
}

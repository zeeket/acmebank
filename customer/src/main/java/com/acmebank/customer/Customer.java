package com.acmebank.customer;

import java.util.List;
import java.util.Collection;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.acmebank.customer.token.Token;

@Entity
@Table(name = "customer")
public class Customer implements UserDetails {

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(type.name()));
    }

    @Id
    @GeneratedValue
    private long id;

    @Enumerated(EnumType.STRING)
    private CustomerType type;

    @OneToMany(mappedBy = "customer")
    private List<Token> tokens;

    @Column(name = "first_name", nullable = true)
    private String firstName;

    @Column(name = "last_name", nullable = true)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String emailToSet) {
        this.email = emailToSet;
    }

    public void setPassword(String passwordToSet) {
        this.password = passwordToSet;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, firstName='%s', lastName='%s']",
                id, firstName, lastName);
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastNameToSet) {
        this.lastName = lastNameToSet;
    }

    public void setFirstName(String firstNameToSet) {
        this.firstName = firstNameToSet;
    }

    public CustomerType getType() {
        return type;
    }

    public void setType(CustomerType typeToSet) {
        this.type = typeToSet;
    }

    public Customer(final String firstName, final String lastName, final String email, final String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public Customer(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public Customer() {
    }

}

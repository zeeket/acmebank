package com.acmebank.customer;

import jakarta.persistence.*;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue
    private long id;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "last_name", nullable = false)
    private String lastName;

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

}

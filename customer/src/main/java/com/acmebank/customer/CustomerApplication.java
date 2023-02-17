package com.acmebank.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import org.slf4j.Logger;


@SpringBootApplication
public class CustomerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerApplication.class, args);
	}

	Logger logger = LoggingController.myLogger();

	@Autowired 
    private CustomerRepository repository; 
  
    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        List<Customer> allCustomers = this.repository.findAll(); 
        logger.info("Number of customers: " + allCustomers.size());
 
        Customer newCustomer = new Customer(); 
        newCustomer.setFirstName("John"); 
        newCustomer.setLastName("Doe"); 
        logger.info("Saving new customer..."); 
        this.repository.save(newCustomer); 
 
        allCustomers = this.repository.findAll(); 
        logger.info("Number of customers: " + allCustomers.size());
    }

}

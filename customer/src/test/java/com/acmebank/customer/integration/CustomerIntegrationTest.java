package com.acmebank.customer.integration;

import com.acmebank.customer.auth.RegisterRequest;
import com.acmebank.customer.customer.Customer;
import com.acmebank.customer.customer.CustomerRepository;
import com.acmebank.customer.customer.CustomerType;
import com.acmebank.customer.token.TokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String tokenString;

    @BeforeAll
    void beforeAll() {
        
        customerRepository.deleteAll();
        tokenRepository.deleteAll();
    }

    @Test
    @Order(1)
    void should_register_a_new_customer() throws Exception {
        final var registerRequest = new RegisterRequest();
        registerRequest.setFirstname("John");
        registerRequest.setLastName("Doe");
        registerRequest.setEmail("john.doe@example.com");
        registerRequest.setPassword("password123");

        MvcResult result = mockMvc.perform(post("/api/v1/auth/register")
                        .content(new ObjectMapper().writeValueAsString(registerRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.token").exists())
                        .andExpect(jsonPath("$.token").isString())
                        .andReturn();
        String response = result.getResponse().getContentAsString();
        String token = JsonPath.parse(response).read("$.token");
        this.tokenString = "Bearer " + token;
    }

    @Test
    @Order(2)
    void should_login_a_customer() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .content("{\"email\":\"john.doe@example.com\",\"password\":\"password123\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.token").exists());
    }

    /* @Test
    @Order(3)
    void should_get_a_customer() throws Exception {
        System.out.println("tokenString: " + tokenString);
        mockMvc.perform(get("/api/v1/customer")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, tokenString))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.firstname").value("John"))
                        .andExpect(jsonPath("$.lastname").value("Doe"))
                        .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                        .andExpect(jsonPath("$.type").value("CONSUMER"));
    } */
}

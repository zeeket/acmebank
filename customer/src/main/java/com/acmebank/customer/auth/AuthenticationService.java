package com.acmebank.customer.auth;

import com.acmebank.customer.config.JwtService;
//import com.acmebank.customer.token.TokenType;
import com.acmebank.customer.token.TokenRepository;
import com.acmebank.customer.token.Token;
//import com.acmebank.customer.CustomerType;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.acmebank.customer.Customer;
import com.acmebank.customer.CustomerRepository;

@Service
public class AuthenticationService {
    private final CustomerRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var customer = (request.getFirstname().isEmpty() || request.getLastname().isEmpty())
                ? new Customer(request.getEmail(), passwordEncoder.encode(request.getPassword()))
                : new Customer(request.getFirstname(), request.getLastname(),
                        request.getEmail(), passwordEncoder.encode(request.getPassword()));

        repository.save(customer);
        var token = jwtService.generateToken(customer);
        saveCustomerToken(customer, token);
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var customer = repository.findByEmail(request.getEmail()).orElseThrow();
        var token = jwtService.generateToken(customer);
        revokeAllCustomerTokens(customer);
        saveCustomerToken(customer, token);
        return new AuthenticationResponse(token);
    }

    private void saveCustomerToken(Customer customer, String token) {
        var tokenToSave = new Token(token, customer);
        tokenRepository.save(tokenToSave);
    }

    private void revokeAllCustomerTokens(Customer customer) {
        var validCustomerTokens = tokenRepository.findAllValidTokenByCustomer(customer.getId());
        validCustomerTokens.forEach(token -> {
            token.revoke();
            token.expire();
        });
        tokenRepository.saveAll(validCustomerTokens);
    }

    public AuthenticationService(final CustomerRepository repository, final TokenRepository tokenRepository,
            final PasswordEncoder passwordEncoder, final JwtService jwtService,
            final AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

}

package com.acmebank.customer.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.acmebank.customer.exception.ErrorDTO;
import com.acmebank.customer.token.TokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private JwtService jwtService;
  private UserDetailsService userDetailsService;
  private TokenRepository tokenRepository;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String userEmail;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }
    jwt = authHeader.substring(7);  
    try {
    userEmail = jwtService.extractUsername(jwt);
    } catch (ExpiredJwtException e) {
      response.setContentType("application/json");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      ErrorDTO jwtExpiredErrorDTO = new ErrorDTO();
      jwtExpiredErrorDTO.setMessage(e.getMessage());
      jwtExpiredErrorDTO.setStatus(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED));
      jwtExpiredErrorDTO.setTime(new Date().toString());
      String json = new ObjectMapper().writeValueAsString(jwtExpiredErrorDTO);
      response.getWriter().write(json);
      return;
    }
    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
      var isTokenValid = tokenRepository.findByToken(jwt)
          .map(t -> !t.isExpired() && !t.isRevoked())
          .orElse(false);
      if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities());
        authToken.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken); 
      } else {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Token invalid");
        return;
      }
    }
    filterChain.doFilter(request, response);
  }

  public JwtAuthFilter(final JwtService jwtService, final UserDetailsService userDetailsService,
      final TokenRepository tokenRepository) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
    this.tokenRepository = tokenRepository;
  }

}
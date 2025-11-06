package com.dev.arthur.springsecurity_jwt_auth.security;

import com.dev.arthur.springsecurity_jwt_auth.repositories.UserRepository;
import com.dev.arthur.springsecurity_jwt_auth.services.auth.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    final TokenService tokenService;
    private final UserRepository userRepository;

    public SecurityFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Extracts the JWT from the Authorization header
        var token = this.recoverToken(request);
        if (token != null) {
            // Validates and extracts the username (subject) from the token
            var username = tokenService.extractSubject(token);

            // Loads user details from the database using the username
            UserDetails user = userRepository.findByUsername(username);

            // Creates an authentication object with the user's authorities
            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            // Sets the authentication in the current SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        // Reads the "Authorization" header from the request
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;

        // Removes the "Bearer " prefix and returns only the token
        return authHeader.replace("Bearer ", "");

    }
}

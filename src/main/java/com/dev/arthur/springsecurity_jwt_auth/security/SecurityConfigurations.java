package com.dev.arthur.springsecurity_jwt_auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyAuthoritiesMapper;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Enables manual Spring Security configuration
@EnableMethodSecurity // Enables @PreAuthorize("hasRole('...')") annotations
public class SecurityConfigurations {

    final SecurityFilter securityFilter;

    public SecurityConfigurations(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable) // Disable CSRF since we're using stateless authentication
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))// No session will be created or used
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")  // Only ADMIN users can POST /products
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll() // Allow everyone to access login
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll() // Open for testing; in production, only ADMIN should create new users
                        .anyRequest().authenticated()) // All other endpoints require authentication
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) // Apply JWT filter before username/password authentication
                .build();
    }

    @Bean

    public RoleHierarchy roleHierarchy() {
        // Defines a role hierarchy where ADMIN inherits all USER permissions
        return RoleHierarchyImpl.fromHierarchy("ROLE_ADMIN > ROLE_USER");
    }

    @Bean

    public RoleHierarchyAuthoritiesMapper authoritiesMapper(RoleHierarchy roleHierarchy) {
        // Maps granted authorities according to the defined role hierarchy
        return new RoleHierarchyAuthoritiesMapper(roleHierarchy);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // Exposes the AuthenticationManager as a Spring Bean to handle authentication requests
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Handles password encryption using the BCrypt hashing algorithm
        return new BCryptPasswordEncoder();
    }
}

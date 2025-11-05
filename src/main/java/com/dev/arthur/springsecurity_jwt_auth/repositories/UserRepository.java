package com.dev.arthur.springsecurity_jwt_auth.repositories;

import com.dev.arthur.springsecurity_jwt_auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    // o springSecurity usa isso para achar o usuario
    UserDetails findByUsername(String username);
}

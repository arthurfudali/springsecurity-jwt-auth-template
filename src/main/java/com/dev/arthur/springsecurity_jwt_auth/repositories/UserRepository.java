package com.dev.arthur.springsecurity_jwt_auth.repositories;

import com.dev.arthur.springsecurity_jwt_auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    // Used by Spring Security
    UserDetails findByUsername(String username);
}

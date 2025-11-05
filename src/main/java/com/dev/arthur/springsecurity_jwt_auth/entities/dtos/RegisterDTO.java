package com.dev.arthur.springsecurity_jwt_auth.entities.dtos;

import com.dev.arthur.springsecurity_jwt_auth.entities.enums.UserRole;

public record RegisterDTO(String username, String password, UserRole role) {
}

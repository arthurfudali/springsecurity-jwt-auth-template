package com.dev.arthur.springsecurity_jwt_auth.entities.enums;

public enum UserRole {
    ROLE_USER("user"),
    ROLE_ADMIN("admin");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}

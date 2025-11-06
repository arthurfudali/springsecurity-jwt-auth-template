package com.dev.arthur.springsecurity_jwt_auth.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class RoleTestController {


    /* This is used to test if the role Hierarchy is working
    *  If you are logged as an ADMIN, both should be available
    *  If you are logged as a USER, '/admin' should be forbidden
    *   */


    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminOnly() {
        return "Acesso ADMIN liberado";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String userOnly() {
        return "Acesso USER liberado";
    }
}
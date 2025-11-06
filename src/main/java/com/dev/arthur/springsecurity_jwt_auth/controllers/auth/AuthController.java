package com.dev.arthur.springsecurity_jwt_auth.controllers.auth;

import com.dev.arthur.springsecurity_jwt_auth.entities.User;
import com.dev.arthur.springsecurity_jwt_auth.entities.dtos.AuthDTO;
import com.dev.arthur.springsecurity_jwt_auth.entities.dtos.RegisterDTO;
import com.dev.arthur.springsecurity_jwt_auth.entities.dtos.TokenResponseDTO;
import com.dev.arthur.springsecurity_jwt_auth.entities.dtos.UserResponseDTO;
import com.dev.arthur.springsecurity_jwt_auth.repositories.UserRepository;
import com.dev.arthur.springsecurity_jwt_auth.services.auth.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager; // essa classe precisa de um Bean no config
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Validated AuthDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new TokenResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Validated RegisterDTO data) {
        if (this.userRepository.findByUsername(data.username()) != null) {
            return ResponseEntity.badRequest().build();
        }
        String encryptedPassword = passwordEncoder.encode(data.password());
        User newUser = new User(data.username(), encryptedPassword, data.role());
        userRepository.save(newUser);
        return ResponseEntity.ok(new UserResponseDTO(newUser.getUsername(), newUser.getRole().name()));
    }
}

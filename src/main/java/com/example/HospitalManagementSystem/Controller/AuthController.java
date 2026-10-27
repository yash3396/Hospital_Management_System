package com.example.HospitalManagementSystem.controller;

import com.example.HospitalManagementSystem.model.User;
import com.example.HospitalManagementSystem.service.UserService;
import com.example.HospitalManagementSystem.view.AuthResponse;
import com.example.HospitalManagementSystem.view.LoginRequest;
import com.example.HospitalManagementSystem.view.RegistrationRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegistrationRequest req) {
        return ResponseEntity.ok(userService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(new AuthResponse(userService.authenticate(req)));
    }

    @GetMapping("/me")
    public ResponseEntity<User> me(Authentication auth) {
        return userService.findByEmail(auth.getName())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

package com.example.HospitalManagementSystem.Controller;

import com.example.HospitalManagementSystem.dto.AuthResponse;
import com.example.HospitalManagementSystem.dto.LoginRequest;
import com.example.HospitalManagementSystem.dto.RegistrationRequest;
import com.example.HospitalManagementSystem.model.User;
import com.example.HospitalManagementSystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest req) {
        User created = userService.register(req);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        String token = userService.authenticate(req);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}

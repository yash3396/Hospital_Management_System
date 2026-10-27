package com.example.HospitalManagementSystem.service;

import com.example.HospitalManagementSystem.view.LoginRequest;
import com.example.HospitalManagementSystem.view.RegistrationRequest;
import com.example.HospitalManagementSystem.model.User;

import java.util.Optional;

public interface UserService {
    User register(RegistrationRequest req);
    String authenticate(LoginRequest req);
    Optional<User> findByEmail(String email);
}

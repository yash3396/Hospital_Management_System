package com.example.HospitalManagementSystem.service;

import com.example.HospitalManagementSystem.dto.LoginRequest;
import com.example.HospitalManagementSystem.dto.RegistrationRequest;
import com.example.HospitalManagementSystem.model.User;

public interface UserService {
    User register(RegistrationRequest req);
    String authenticate(LoginRequest req);
}

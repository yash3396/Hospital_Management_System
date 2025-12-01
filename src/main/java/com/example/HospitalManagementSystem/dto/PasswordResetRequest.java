package com.example.HospitalManagementSystem.dto;

public class PasswordResetRequest {
    private String password;

    public PasswordResetRequest() {}

    public PasswordResetRequest(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

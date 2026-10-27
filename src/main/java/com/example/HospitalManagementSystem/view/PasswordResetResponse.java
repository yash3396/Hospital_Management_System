package com.example.HospitalManagementSystem.view;

public class PasswordResetResponse {
    private String temporaryPassword;

    public PasswordResetResponse() {}

    public PasswordResetResponse(String temporaryPassword) {
        this.temporaryPassword = temporaryPassword;
    }

    public String getTemporaryPassword() {
        return temporaryPassword;
    }

    public void setTemporaryPassword(String temporaryPassword) {
        this.temporaryPassword = temporaryPassword;
    }
}

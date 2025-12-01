package com.example.HospitalManagementSystem.dto;

import jakarta.validation.constraints.NotBlank;

public class CheckinRequest {
    @NotBlank
    private String status; // e.g. "CHECKED_IN"

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

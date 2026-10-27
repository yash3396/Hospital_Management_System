package com.example.HospitalManagementSystem.view;

import jakarta.validation.constraints.NotBlank;

public class LabStatusRequest {
    @NotBlank
    private String status;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

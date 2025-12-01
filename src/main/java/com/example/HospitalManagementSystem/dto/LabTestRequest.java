package com.example.HospitalManagementSystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LabTestRequest {
    @NotNull
    private Long appointmentId;

    @NotBlank
    private String testName;

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }
}

package com.example.HospitalManagementSystem.view;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AppointmentBookingRequest {
    @NotNull
    private Long doctorId;

    @NotBlank
    private String date; // ISO yyyy-MM-dd

    @NotBlank
    private String slot;

    private String symptoms;

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getSlot() { return slot; }
    public void setSlot(String slot) { this.slot = slot; }
    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
}

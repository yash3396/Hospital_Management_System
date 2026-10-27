package com.example.HospitalManagementSystem.view;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class ScheduleRequest {
    @NotNull
    private Long doctorId;

    @NotNull
    private String date; // ISO yyyy-MM-dd

    @NotNull
    @Size(min = 1)
    private List<String> slots;

    public ScheduleRequest() {}

    public ScheduleRequest(Long doctorId, String date, List<String> slots) {
        this.doctorId = doctorId;
        this.date = date;
        this.slots = slots;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getSlots() {
        return slots;
    }

    public void setSlots(List<String> slots) {
        this.slots = slots;
    }
}

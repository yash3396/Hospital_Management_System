package com.example.HospitalManagementSystem.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private Long id;
    private Doctor doctor;
    private LocalDate date;
    private List<String> slots = new ArrayList<>();

    public Schedule() {
    }

    public Schedule(Doctor doctor, LocalDate date, List<String> slots) {
        this.doctor = doctor;
        this.date = date;
        this.slots = slots != null ? new ArrayList<>(slots) : new ArrayList<>();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public List<String> getSlots() { return slots; }
    public void setSlots(List<String> slots) { this.slots = slots; }
}

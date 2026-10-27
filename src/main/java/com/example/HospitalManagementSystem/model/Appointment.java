package com.example.HospitalManagementSystem.model;

import java.time.LocalDate;

public class Appointment {
    private Long id;
    private User patient;
    private Doctor doctor;
    private LocalDate date;
    private String slot;
    private String symptoms;
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    public Appointment() {
    }

    public Appointment(User patient, Doctor doctor, LocalDate date, String slot, String symptoms) {
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.slot = slot;
        this.symptoms = symptoms;
        this.status = AppointmentStatus.SCHEDULED;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getPatient() { return patient; }
    public void setPatient(User patient) { this.patient = patient; }
    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getSlot() { return slot; }
    public void setSlot(String slot) { this.slot = slot; }
    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
}

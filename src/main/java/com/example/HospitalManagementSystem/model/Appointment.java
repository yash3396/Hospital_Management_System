package com.example.HospitalManagementSystem.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "appointments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"doctor_id", "date", "slot", "patient_id"}))
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // patient stored as User
    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id")
    private User patient;

    @ManyToOne(optional = false)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String slot;

    @Column(length = 1000)
    private String symptoms;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    public Appointment() {}

    public Appointment(User patient, Doctor doctor, LocalDate date, String slot, String symptoms) {
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.slot = slot;
        this.symptoms = symptoms;
        this.status = AppointmentStatus.SCHEDULED;
    }

    public Long getId() { return id; }
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

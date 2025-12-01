package com.example.HospitalManagementSystem.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedules",
        uniqueConstraints = @UniqueConstraint(columnNames = {"doctor_id", "date"}))
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Column(nullable = false)
    private LocalDate date;

    @ElementCollection
    @CollectionTable(name = "schedule_slots", joinColumns = @JoinColumn(name = "schedule_id"))
    @Column(name = "slot")
    private List<String> slots = new ArrayList<>();

    public Schedule() {
    }

    // Explicit constructor matching the call in AdminServiceImpl
    public Schedule(Doctor doctor, LocalDate date, List<String> slots) {
        this.doctor = doctor;
        this.date = date;
        if (slots != null) {
            this.slots = new ArrayList<>(slots);
        } else {
            this.slots = new ArrayList<>();
        }
    }

    public Long getId() {
        return id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<String> getSlots() {
        return slots;
    }

    public void setSlots(List<String> slots) {
        this.slots = slots;
    }
}

package com.example.HospitalManagementSystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "prescriptions")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "appointment_id", unique = true)
    private Appointment appointment;

    @Column(length = 2000)
    private String diagnosis;

    @Column(length = 2000)
    private String medicine; // free text for now

    private boolean dispensed = false;

    public Prescription() {}

    public Prescription(Appointment appointment, String diagnosis, String medicine) {
        this.appointment = appointment;
        this.diagnosis = diagnosis;
        this.medicine = medicine;
    }

    public Long getId() { return id; }
    public Appointment getAppointment() { return appointment; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getMedicine() { return medicine; }
    public void setMedicine(String medicine) { this.medicine = medicine; }
    public boolean isDispensed() { return dispensed; }
    public void setDispensed(boolean dispensed) { this.dispensed = dispensed; }
}

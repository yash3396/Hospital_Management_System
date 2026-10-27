package com.example.HospitalManagementSystem.model;

public class Prescription {
    private Long id;
    private Appointment appointment;
    private String diagnosis;
    private String medicine;
    private boolean dispensed;

    public Prescription() {
    }

    public Prescription(Appointment appointment, String diagnosis, String medicine) {
        this.appointment = appointment;
        this.diagnosis = diagnosis;
        this.medicine = medicine;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Appointment getAppointment() { return appointment; }
    public void setAppointment(Appointment appointment) { this.appointment = appointment; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getMedicine() { return medicine; }
    public void setMedicine(String medicine) { this.medicine = medicine; }
    public boolean isDispensed() { return dispensed; }
    public void setDispensed(boolean dispensed) { this.dispensed = dispensed; }
}

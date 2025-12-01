package com.example.HospitalManagementSystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "lab_tests")
public class LabTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(nullable = false)
    private String testName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private LabTestStatus status = LabTestStatus.PENDING;

    @Column(length = 4000)
    private String result;

    public LabTest() {}

    public LabTest(Appointment appointment, String testName) {
        this.appointment = appointment;
        this.testName = testName;
    }

    public Long getId() { return id; }
    public Appointment getAppointment() { return appointment; }
    public String getTestName() { return testName; }
    public LabTestStatus getStatus() { return status; }
    public void setStatus(LabTestStatus status) { this.status = status; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
}

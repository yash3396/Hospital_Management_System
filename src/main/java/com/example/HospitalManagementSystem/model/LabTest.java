package com.example.HospitalManagementSystem.model;

public class LabTest {
    private Long id;
    private Appointment appointment;
    private String testName;
    private LabTestStatus status = LabTestStatus.PENDING;
    private String result;

    public LabTest() {
    }

    public LabTest(Appointment appointment, String testName) {
        this.appointment = appointment;
        this.testName = testName;
        this.status = LabTestStatus.PENDING;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Appointment getAppointment() { return appointment; }
    public void setAppointment(Appointment appointment) { this.appointment = appointment; }
    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }
    public LabTestStatus getStatus() { return status; }
    public void setStatus(LabTestStatus status) { this.status = status; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
}

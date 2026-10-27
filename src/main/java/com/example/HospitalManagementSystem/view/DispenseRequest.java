package com.example.HospitalManagementSystem.view;

import jakarta.validation.constraints.NotNull;

public class DispenseRequest {
    @NotNull
    private Long prescriptionId;

    public Long getPrescriptionId() { return prescriptionId; }
    public void setPrescriptionId(Long prescriptionId) { this.prescriptionId = prescriptionId; }
}

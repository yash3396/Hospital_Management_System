package com.example.HospitalManagementSystem.view;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LabResultRequest {
    @NotNull
    private Long testRequestId;

    @NotBlank
    private String result;

    public Long getTestRequestId() { return testRequestId; }
    public void setTestRequestId(Long testRequestId) { this.testRequestId = testRequestId; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
}

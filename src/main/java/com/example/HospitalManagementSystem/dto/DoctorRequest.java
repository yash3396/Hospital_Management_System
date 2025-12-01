package com.example.HospitalManagementSystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DoctorRequest {
    @NotBlank
    private String name;

    private String specialization;

    @Email
    @NotBlank
    private String email;

    private String phone;

    @NotNull
    private Long departmentId;

    public DoctorRequest() {}

    public DoctorRequest(String name, String specialization, String email, String phone, Long departmentId) {
        this.name = name;
        this.specialization = specialization;
        this.email = email;
        this.phone = phone;
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}

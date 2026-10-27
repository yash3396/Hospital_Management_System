package com.example.HospitalManagementSystem.view;

import jakarta.validation.constraints.NotBlank;

public class RoleAssignRequest {
    @NotBlank
    private String role; // e.g. "DOCTOR" or "ROLE_DOCTOR"

    public RoleAssignRequest() {}

    public RoleAssignRequest(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

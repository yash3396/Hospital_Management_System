package com.example.HospitalManagementSystem.Controller;

import com.example.HospitalManagementSystem.dto.*;
import com.example.HospitalManagementSystem.model.Department;
import com.example.HospitalManagementSystem.model.Doctor;
import com.example.HospitalManagementSystem.model.Schedule;
import com.example.HospitalManagementSystem.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/doctors")
    public ResponseEntity<Doctor> addDoctor(@Valid @RequestBody DoctorRequest req) {
        Doctor created = adminService.addDoctor(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/doctors/{doctorId}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long doctorId, @Valid @RequestBody DoctorRequest req) {
        Doctor updated = adminService.updateDoctor(doctorId, req);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/doctors/{doctorId}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long doctorId) {
        adminService.deleteDoctor(doctorId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/departments")
    public ResponseEntity<Department> addDepartment(@Valid @RequestBody DepartmentRequest req) {
        Department d = adminService.addDepartment(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(d);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(adminService.getAllDepartments());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/schedules")
    public ResponseEntity<Schedule> createSchedule(@Valid @RequestBody ScheduleRequest req) {
        Schedule s = adminService.createSchedule(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(s);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{id}/role")
    public ResponseEntity<Void> assignRole(@PathVariable Long id, @Valid @RequestBody RoleAssignRequest req) {
        adminService.assignRoleToUser(id, req.getRole());
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{id}/password")
    public ResponseEntity<PasswordResetResponse> resetUserPassword(@PathVariable Long id, @RequestBody PasswordResetRequest req) {
        String newPlain = (req != null) ? req.getPassword() : null;
        String plainPassword = adminService.resetUserPassword(id, newPlain);
        // return the temporary/plain password to the caller (only admins can call this).
        return ResponseEntity.ok(new PasswordResetResponse(plainPassword));
    }
}

package com.example.HospitalManagementSystem.controller;

import com.example.HospitalManagementSystem.view.LabTestRequest;
import com.example.HospitalManagementSystem.view.PrescriptionRequest;
import com.example.HospitalManagementSystem.model.Appointment;
import com.example.HospitalManagementSystem.model.LabTest;
import com.example.HospitalManagementSystem.model.Prescription;
import com.example.HospitalManagementSystem.service.HospitalService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
    private final HospitalService hospitalService;

    public DoctorController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAppointments(Authentication auth) {
        String email = auth.getName();
        return ResponseEntity.ok(hospitalService.getDoctorAppointmentsByEmail(email));
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping("/prescriptions")
    public ResponseEntity<Prescription> addPrescription(@Valid @RequestBody PrescriptionRequest req, Authentication auth) {
        String email = auth.getName();
        Prescription p = hospitalService.createOrUpdatePrescription(req, email);
        return ResponseEntity.ok(p);
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping("/lab-tests")
    public ResponseEntity<LabTest> requestLabTest(@Valid @RequestBody LabTestRequest req, Authentication auth) {
        String email = auth.getName();
        LabTest t = hospitalService.requestLabTest(req, email);
        return ResponseEntity.ok(t);
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/patients/{patientId}/history")
    public ResponseEntity<List<Appointment>> getPatientHistory(@PathVariable Long patientId, Authentication auth) {
        String email = auth.getName();
        return ResponseEntity.ok(hospitalService.getPatientHistory(patientId, email));
    }
}

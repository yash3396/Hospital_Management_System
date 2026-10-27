package com.example.HospitalManagementSystem.controller;

import com.example.HospitalManagementSystem.view.AppointmentBookingRequest;
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
@RequestMapping("/api/patient")
public class PatientController {
    private final HospitalService hospitalService;

    public PatientController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/appointments")
    public ResponseEntity<Appointment> bookAppointment(@Valid @RequestBody AppointmentBookingRequest req, Authentication auth) {
        Appointment a = hospitalService.bookAppointment(auth.getName(), req);
        return ResponseEntity.ok(a);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> myAppointments(Authentication auth) {
        return ResponseEntity.ok(hospitalService.getPatientAppointments(auth.getName()));
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/prescriptions")
    public ResponseEntity<List<Prescription>> myPrescriptions(Authentication auth) {
        return ResponseEntity.ok(hospitalService.getPatientPrescriptions(auth.getName()));
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/lab-results")
    public ResponseEntity<List<LabTest>> myLabResults(Authentication auth) {
        return ResponseEntity.ok(hospitalService.getPatientLabResults(auth.getName()));
    }
}

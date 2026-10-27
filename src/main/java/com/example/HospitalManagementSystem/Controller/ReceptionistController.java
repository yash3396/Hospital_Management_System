package com.example.HospitalManagementSystem.controller;

import com.example.HospitalManagementSystem.model.Appointment;
import com.example.HospitalManagementSystem.model.Doctor;
import com.example.HospitalManagementSystem.model.User;
import com.example.HospitalManagementSystem.service.HospitalService;
import com.example.HospitalManagementSystem.view.AppointmentBookingRequest;
import com.example.HospitalManagementSystem.view.CheckinRequest;
import com.example.HospitalManagementSystem.view.ReceptionistPatientRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/receptionist")
public class ReceptionistController {
    private final HospitalService hospitalService;

    public ReceptionistController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @PreAuthorize("hasRole('RECEPTIONIST')")
    @GetMapping("/patients")
    public ResponseEntity<List<User>> listPatients() {
        return ResponseEntity.ok(hospitalService.listPatients());
    }

    @PreAuthorize("hasRole('RECEPTIONIST')")
    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> listDoctors() {
        return ResponseEntity.ok(hospitalService.listDoctors());
    }

    @PreAuthorize("hasRole('RECEPTIONIST')")
    @PostMapping("/patients")
    public ResponseEntity<User> registerPatient(@Valid @RequestBody ReceptionistPatientRequest req) {
        return ResponseEntity.status(201).body(hospitalService.registerPatientByReceptionist(req));
    }

    @PreAuthorize("hasRole('RECEPTIONIST')")
    @PostMapping("/appointments")
    public ResponseEntity<Appointment> bookAppointmentForPatient(@Valid @RequestBody AppointmentBookingRequest req, @RequestParam Long patientId) {
        return ResponseEntity.status(201).body(hospitalService.bookAppointmentForPatientByReceptionist(patientId, req));
    }

    @PreAuthorize("hasRole('RECEPTIONIST')")
    @PutMapping("/appointments/{id}/checkin")
    public ResponseEntity<Appointment> checkin(@PathVariable Long id, @RequestBody CheckinRequest req) {
        return ResponseEntity.ok(hospitalService.checkinAppointment(id, req.getStatus()));
    }
}

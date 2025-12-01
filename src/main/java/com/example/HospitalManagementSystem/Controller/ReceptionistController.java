package com.example.HospitalManagementSystem.Controller;

import com.example.HospitalManagementSystem.dto.AppointmentBookingRequest;
import com.example.HospitalManagementSystem.dto.CheckinRequest;
import com.example.HospitalManagementSystem.dto.ReceptionistPatientRequest;
import com.example.HospitalManagementSystem.model.Appointment;
import com.example.HospitalManagementSystem.model.User;
import com.example.HospitalManagementSystem.service.HospitalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/receptionist")
public class ReceptionistController {
    private final HospitalService hospitalService;

    public ReceptionistController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @PreAuthorize("hasRole('RECEPTIONIST')")
    @PostMapping("/patients")
    public ResponseEntity<User> registerPatient(@Valid @RequestBody ReceptionistPatientRequest req) {
        User u = hospitalService.registerPatientByReceptionist(req);
        return ResponseEntity.status(201).body(u);
    }

    @PreAuthorize("hasRole('RECEPTIONIST')")
    @PostMapping("/appointments")
    public ResponseEntity<Appointment> bookAppointmentForPatient(@Valid @RequestBody AppointmentBookingRequest req, @RequestParam Long patientId) {
        Appointment a = hospitalService.bookAppointmentForPatientByReceptionist(patientId, req);
        return ResponseEntity.status(201).body(a);
    }

    @PreAuthorize("hasRole('RECEPTIONIST')")
    @PutMapping("/appointments/{id}/checkin")
    public ResponseEntity<Appointment> checkin(@PathVariable Long id, @RequestBody CheckinRequest req) {
        Appointment a = hospitalService.checkinAppointment(id, req.getStatus());
        return ResponseEntity.ok(a);
    }
}

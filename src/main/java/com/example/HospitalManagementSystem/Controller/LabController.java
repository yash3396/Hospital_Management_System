package com.example.HospitalManagementSystem.controller;

import com.example.HospitalManagementSystem.model.LabTest;
import com.example.HospitalManagementSystem.service.HospitalService;
import com.example.HospitalManagementSystem.view.LabResultRequest;
import com.example.HospitalManagementSystem.view.LabStatusRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lab")
public class LabController {
    private final HospitalService hospitalService;

    public LabController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @PreAuthorize("hasRole('LABTECHNICIAN')")
    @PostMapping("/results")
    public ResponseEntity<LabTest> addResult(@Valid @RequestBody LabResultRequest body) {
        return ResponseEntity.ok(hospitalService.addLabResult(body.getTestRequestId(), body.getResult()));
    }

    @PreAuthorize("hasRole('LABTECHNICIAN')")
    @PutMapping("/tests/{testId}")
    public ResponseEntity<LabTest> updateStatus(@PathVariable Long testId, @Valid @RequestBody LabStatusRequest body) {
        return ResponseEntity.ok(hospitalService.updateLabTestStatus(testId, body.getStatus()));
    }

    @PreAuthorize("hasRole('LABTECHNICIAN')")
    @GetMapping("/tests/pending")
    public ResponseEntity<List<LabTest>> pendingTests() {
        return ResponseEntity.ok(hospitalService.getPendingLabTests());
    }
}

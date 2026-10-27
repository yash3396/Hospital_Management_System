package com.example.HospitalManagementSystem.controller;

import com.example.HospitalManagementSystem.model.Doctor;
import com.example.HospitalManagementSystem.service.HospitalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {
    private final HospitalService hospitalService;

    public CatalogController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> listDoctors() {
        return ResponseEntity.ok(hospitalService.listDoctors());
    }
}

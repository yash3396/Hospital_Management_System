package com.example.HospitalManagementSystem.controller;

import com.example.HospitalManagementSystem.view.DispenseRequest;
import com.example.HospitalManagementSystem.view.StockUpdateRequest;
import com.example.HospitalManagementSystem.model.MedicineStock;
import com.example.HospitalManagementSystem.model.Prescription;
import com.example.HospitalManagementSystem.service.HospitalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacy")
public class PharmacyController {
    private final HospitalService hospitalService;

    public PharmacyController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @PreAuthorize("hasRole('PHARMACIST')")
    @PostMapping("/dispense")
    public ResponseEntity<Prescription> dispense(@Valid @RequestBody DispenseRequest req) {
        Prescription p = hospitalService.dispenseMedicine(req.getPrescriptionId());
        return ResponseEntity.ok(p);
    }

    @PreAuthorize("hasRole('PHARMACIST')")
    @PutMapping("/stock")
    public ResponseEntity<MedicineStock> updateStock(@Valid @RequestBody StockUpdateRequest req) {
        return ResponseEntity.ok(hospitalService.updateMedicineStock(req));
    }

    @PreAuthorize("hasRole('PHARMACIST')")
    @GetMapping("/prescriptions")
    public ResponseEntity<List<Prescription>> prescriptionsToDispense() {
        return ResponseEntity.ok(hospitalService.getPrescriptionsToDispense());
    }
}

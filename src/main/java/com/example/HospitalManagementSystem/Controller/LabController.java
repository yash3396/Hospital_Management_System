package com.example.HospitalManagementSystem.Controller;

import com.example.HospitalManagementSystem.model.LabTest;
import com.example.HospitalManagementSystem.service.HospitalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lab")
public class LabController {
    private final HospitalService hospitalService;

    public LabController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @PreAuthorize("hasRole('LABTECHNICIAN')")
    @PostMapping("/results")
    public ResponseEntity<LabTest> addResult(@RequestBody Map<String, String> body,Authentication auth) {
        Long testRequestId = Long.valueOf(body.get("testRequestId"));
        String result = body.get("result");
        LabTest t = hospitalService.addLabResult(testRequestId, result);
        return ResponseEntity.ok(t);
    }

    @PreAuthorize("hasRole('LABTECHNICIAN')")
    @PutMapping("/tests/{testId}")
    public ResponseEntity<LabTest> updateStatus(@PathVariable Long testId, @RequestBody Map<String, String> body,Authentication auth) {
        String status = body.get("status");
        LabTest t = hospitalService.updateLabTestStatus(testId, status);
        return ResponseEntity.ok(t);
    }

    @PreAuthorize("hasRole('LABTECHNICIAN')")
    @GetMapping("/tests/pending")
    public ResponseEntity<List<LabTest>> pendingTests(Authentication auth) {
        return ResponseEntity.ok(hospitalService.getPendingLabTests());
    }
}

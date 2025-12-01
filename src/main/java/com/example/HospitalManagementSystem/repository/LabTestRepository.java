package com.example.HospitalManagementSystem.repository;

import com.example.HospitalManagementSystem.model.LabTest;
import com.example.HospitalManagementSystem.model.LabTestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabTestRepository extends JpaRepository<LabTest, Long> {
    List<LabTest> findByStatus(LabTestStatus status);
    List<LabTest> findByAppointmentPatientId(Long patientId);
}

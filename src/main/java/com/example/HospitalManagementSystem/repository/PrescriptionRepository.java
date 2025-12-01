package com.example.HospitalManagementSystem.repository;

import com.example.HospitalManagementSystem.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByAppointmentPatientId(Long patientId);
    List<Prescription> findByDispensedFalse();
}

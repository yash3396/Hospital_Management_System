package com.example.HospitalManagementSystem.repository;

import com.example.HospitalManagementSystem.model.MedicineStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicineStockRepository extends JpaRepository<MedicineStock, Long> {
    Optional<MedicineStock> findByMedicineNameIgnoreCase(String name);
}

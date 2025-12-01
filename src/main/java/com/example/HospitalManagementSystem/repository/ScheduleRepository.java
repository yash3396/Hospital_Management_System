package com.example.HospitalManagementSystem.repository;

import com.example.HospitalManagementSystem.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByDoctorIdAndDate(Long doctorId, LocalDate date);
}

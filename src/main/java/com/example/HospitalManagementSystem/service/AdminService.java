package com.example.HospitalManagementSystem.service;

import com.example.HospitalManagementSystem.dto.DepartmentRequest;
import com.example.HospitalManagementSystem.dto.DoctorRequest;
import com.example.HospitalManagementSystem.dto.ScheduleRequest;
import com.example.HospitalManagementSystem.model.Department;
import com.example.HospitalManagementSystem.model.Doctor;
import com.example.HospitalManagementSystem.model.Schedule;

import java.util.List;

public interface AdminService {
    Doctor addDoctor(DoctorRequest req);
    Doctor updateDoctor(Long id, DoctorRequest req);
    void deleteDoctor(Long id);
    Department addDepartment(DepartmentRequest req);
    List<Department> getAllDepartments();
    Schedule createSchedule(ScheduleRequest req);
    void assignRoleToUser(Long userId, String roleName);
    String resetUserPassword(Long userId, String newPlainPassword);
}

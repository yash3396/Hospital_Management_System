package com.example.HospitalManagementSystem.service;

import com.example.HospitalManagementSystem.view.DepartmentRequest;
import com.example.HospitalManagementSystem.view.DoctorRequest;
import com.example.HospitalManagementSystem.view.ScheduleRequest;
import com.example.HospitalManagementSystem.model.Department;
import com.example.HospitalManagementSystem.model.Doctor;
import com.example.HospitalManagementSystem.model.Schedule;

import java.util.List;

public interface AdminService {
    Doctor addDoctor(DoctorRequest req);
    Doctor updateDoctor(Long id, DoctorRequest req);
    void deleteDoctor(Long id);
    List<Doctor> getAllDoctors();
    Department addDepartment(DepartmentRequest req);
    List<Department> getAllDepartments();
    List<com.example.HospitalManagementSystem.model.User> getAllUsers();
    Schedule createSchedule(ScheduleRequest req);
    void assignRoleToUser(Long userId, String roleName);
    String resetUserPassword(Long userId, String newPlainPassword);
}

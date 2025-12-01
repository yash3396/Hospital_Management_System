package com.example.HospitalManagementSystem.Config;

import com.example.HospitalManagementSystem.model.Role;
import com.example.HospitalManagementSystem.model.RoleName;
import com.example.HospitalManagementSystem.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public void run(String... args) throws Exception {
        // Seed roles
        if (roleRepository.findByName(RoleName.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(new Role(RoleName.ROLE_ADMIN));
        }
        if (roleRepository.findByName(RoleName.ROLE_PATIENT).isEmpty()) {
            roleRepository.save(new Role(RoleName.ROLE_PATIENT));
        }
        if (roleRepository.findByName(RoleName.ROLE_PHARMACIST).isEmpty()) {
            roleRepository.save(new Role(RoleName.ROLE_PHARMACIST));
        }
        if (roleRepository.findByName(RoleName.ROLE_DOCTOR).isEmpty()) {
            roleRepository.save(new Role(RoleName.ROLE_DOCTOR));
        }
        if (roleRepository.findByName(RoleName.ROLE_RECEPTIONIST).isEmpty()) {
            roleRepository.save(new Role(RoleName.ROLE_RECEPTIONIST));
        }
        if (roleRepository.findByName(RoleName.ROLE_LABTECHNICIAN).isEmpty()) {
            roleRepository.save(new Role(RoleName.ROLE_LABTECHNICIAN));
        }
    }
}

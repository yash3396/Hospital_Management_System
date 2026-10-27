package com.example.HospitalManagementSystem.Config;

import com.example.HospitalManagementSystem.model.Role;
import com.example.HospitalManagementSystem.model.RoleName;
import com.example.HospitalManagementSystem.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        for (RoleName name : RoleName.values()) {
            if (roleRepository.findByName(name).isEmpty()) {
                roleRepository.save(new Role(name));
            }
        }
    }
}

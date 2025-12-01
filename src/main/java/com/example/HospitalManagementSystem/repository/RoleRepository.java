package com.example.HospitalManagementSystem.repository;

import com.example.HospitalManagementSystem.model.Role;
import com.example.HospitalManagementSystem.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}

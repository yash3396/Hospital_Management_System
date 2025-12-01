package com.example.HospitalManagementSystem.service;

import com.example.HospitalManagementSystem.dto.DepartmentRequest;
import com.example.HospitalManagementSystem.dto.DoctorRequest;
import com.example.HospitalManagementSystem.dto.ScheduleRequest;
import com.example.HospitalManagementSystem.model.*;
import com.example.HospitalManagementSystem.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(DoctorRepository doctorRepository,
                            DepartmentRepository departmentRepository,
                            ScheduleRepository scheduleRepository,
                            UserRepository userRepository,
                            RoleRepository roleRepository,
                            PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public Doctor addDoctor(DoctorRequest req) {
        Department dept = departmentRepository.findById(req.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department not found: " + req.getDepartmentId()));

        // Create or reuse User for doctor so doctor can log in
        Optional<User> existingUserOpt = userRepository.findByEmail(req.getEmail());
        User user;
        Role doctorRole = roleRepository.findByName(RoleName.ROLE_DOCTOR)
                .orElseGet(() -> roleRepository.save(new Role(RoleName.ROLE_DOCTOR)));

        if (existingUserOpt.isPresent()) {
            user = existingUserOpt.get();
            // ensure the user has ROLE_DOCTOR
            boolean hasDoctorRole = user.getRoles().stream().anyMatch(r -> r.getName() == RoleName.ROLE_DOCTOR);
            if (!hasDoctorRole) {
                user.addRole(doctorRole);
                userRepository.save(user);
            }
        } else {
            // generate a temporary password for the doctor (24 characters from allowed set)
            String plainPassword = generateRandomPassword(12);
            user = new User(req.getName(), req.getEmail(), passwordEncoder.encode(plainPassword));
            user.setPhone(req.getPhone());
            // no address provided here - can be set later
            user.addRole(doctorRole);
            user = userRepository.save(user);
            // NOTE: we do not expose the plain password here. Consider returning it to the caller
            // or sending an email to the doctor to set a password. For now, an admin can reset it later.
        }

        Doctor d = new Doctor(req.getName(), req.getSpecialization(), req.getEmail(), req.getPhone(), dept);
        d.setUser(user);
        return doctorRepository.save(d);
    }
    @Transactional
    public String resetUserPassword(Long userId, String newPlainPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        String plain;
        if (newPlainPassword == null || newPlainPassword.isBlank()) {
            plain = generateRandomPassword(12);
        } else {
            plain = newPlainPassword;
        }
        user.setPassword(passwordEncoder.encode(plain));
        userRepository.save(user);
        return plain;
    }

    @Transactional
    public Doctor updateDoctor(Long id, DoctorRequest req) {
        Doctor d = doctorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + id));
        d.setName(req.getName());
        d.setSpecialization(req.getSpecialization());
        d.setEmail(req.getEmail());
        d.setPhone(req.getPhone());
        if (req.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(req.getDepartmentId())
                    .orElseThrow(() -> new IllegalArgumentException("Department not found: " + req.getDepartmentId()));
            d.setDepartment(dept);
        }

        // If email changed, update linked user email if present and no conflict
        User linked = d.getUser();
        if (linked != null && !linked.getEmail().equals(req.getEmail())) {
            if (userRepository.existsByEmail(req.getEmail())) {
                throw new IllegalArgumentException("Email already in use: " + req.getEmail());
            }
            linked.setEmail(req.getEmail());
            userRepository.save(linked);
            d.setUser(linked);
        }

        return doctorRepository.save(d);
    }


    @Transactional
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new IllegalArgumentException("Doctor not found: " + id);
        }
        // optionally delete linked user? For safety we will not remove the User automatically.
        // If you want to delete the user as well, uncomment below.
        /*
        doctorRepository.findById(id).ifPresent(d -> {
            if (d.getUser() != null) {
                userRepository.deleteById(d.getUser().getId());
            }
        });
         */
        doctorRepository.deleteById(id);
    }


    @Transactional
    public Department addDepartment(DepartmentRequest req) {
        if (departmentRepository.findByName(req.getName()).isPresent()) {
            throw new IllegalArgumentException("Department already exists: " + req.getName());
        }
        Department d = new Department(req.getName(), req.getDescription());
        return departmentRepository.save(d);
    }


    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }


    @Transactional
    public Schedule createSchedule(ScheduleRequest req) {
        Doctor doctor = doctorRepository.findById(req.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + req.getDoctorId()));
        LocalDate date = LocalDate.parse(req.getDate());
        // If schedule exists for same doctor & date, replace slots (could be changed to merge/append)
        scheduleRepository.findByDoctorIdAndDate(doctor.getId(), date)
                .ifPresent(scheduleRepository::delete);

        Schedule schedule = new Schedule(doctor, date, req.getSlots());
        return scheduleRepository.save(schedule);
    }


    @Transactional
    public void assignRoleToUser(Long userId, String roleName) {
        com.example.HospitalManagementSystem.model.User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        String normalized = roleName.trim().toUpperCase();
        RoleName rn;
        try {
            if (!normalized.startsWith("ROLE_")) {
                normalized = "ROLE_" + normalized;
            }
            rn = RoleName.valueOf(normalized);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid role: " + roleName);
        }
        Role role = roleRepository.findByName(rn).orElseGet(() -> roleRepository.save(new Role(rn)));
        user.addRole(role);
        userRepository.save(user);
    }

    // ---------------------
    private String generateRandomPassword(int length) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%&*";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}

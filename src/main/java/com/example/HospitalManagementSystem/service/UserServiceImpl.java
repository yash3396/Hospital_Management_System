package com.example.HospitalManagementSystem.service;

import com.example.HospitalManagementSystem.view.LoginRequest;
import com.example.HospitalManagementSystem.view.RegistrationRequest;
import com.example.HospitalManagementSystem.model.Role;
import com.example.HospitalManagementSystem.model.RoleName;
import com.example.HospitalManagementSystem.model.User;
import com.example.HospitalManagementSystem.repository.RoleRepository;
import com.example.HospitalManagementSystem.repository.UserRepository;
import com.example.HospitalManagementSystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;


    @Transactional
    public User register(RegistrationRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = new User(req.getName(), req.getEmail(), passwordEncoder.encode(req.getPassword()));
        user.setPhone(req.getPhone());
        user.setAddress(req.getAddress());

        RoleName rn;
        try {
            rn = RoleName.valueOf(req.getRole());
        } catch (Exception e) {
            rn = RoleName.ROLE_PATIENT;
        }
        Role role = roleRepository.findByName(rn).orElse(null);
        if (role == null) {
            role = roleRepository.save(new Role(rn));
        }
        user.addRole(role);
        return userRepository.save(user);
    }


    public String authenticate(LoginRequest req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails ud = (UserDetails) authentication.getPrincipal();
        return jwtUtil.generateToken(ud);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

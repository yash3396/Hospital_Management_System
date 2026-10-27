package com.example.HospitalManagementSystem.repository;

import org.springframework.jdbc.support.KeyHolder;

final class JdbcKeys {
    private JdbcKeys() {
    }

    static Long id(KeyHolder keys) {
        Number generated = keys.getKey();
        return generated == null ? null : generated.longValue();
    }
}

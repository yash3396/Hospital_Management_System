package com.example.HospitalManagementSystem.repository;

import com.example.HospitalManagementSystem.model.Department;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class DepartmentRepository {
    private static final RowMapper<Department> MAPPER = (rs, rowNum) -> {
        Department department = new Department();
        department.setId(rs.getLong("id"));
        department.setName(rs.getString("name"));
        department.setDescription(rs.getString("description"));
        return department;
    };

    private final JdbcTemplate jdbc;

    public DepartmentRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<Department> findById(Long id) {
        return jdbc.query("SELECT id, name, description FROM departments WHERE id = ?", MAPPER, id)
                .stream()
                .findFirst();
    }

    public Optional<Department> findByName(String name) {
        return jdbc.query("SELECT id, name, description FROM departments WHERE name = ?", MAPPER, name)
                .stream()
                .findFirst();
    }

    public List<Department> findAll() {
        return jdbc.query("SELECT id, name, description FROM departments ORDER BY id", MAPPER);
    }

    public Department save(Department department) {
        if (department.getId() != null) {
            jdbc.update("UPDATE departments SET name = ?, description = ? WHERE id = ?",
                    department.getName(), department.getDescription(), department.getId());
            return department;
        }
        KeyHolder keys = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO departments (name, description) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, department.getName());
            ps.setString(2, department.getDescription());
            return ps;
        }, keys);
        department.setId(JdbcKeys.id(keys));
        return department;
    }
}

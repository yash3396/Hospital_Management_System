package com.example.HospitalManagementSystem.repository;

import com.example.HospitalManagementSystem.model.Department;
import com.example.HospitalManagementSystem.model.Doctor;
import com.example.HospitalManagementSystem.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
public class DoctorRepository {
    private static final String SELECT_SQL = """
            SELECT d.id, d.name, d.specialization, d.email, d.phone, d.user_id,
                   dep.id AS department_id, dep.name AS department_name, dep.description AS department_description,
                   u.id AS linked_user_id, u.name AS user_name, u.email AS user_email, u.password AS user_password
            FROM doctors d
            LEFT JOIN departments dep ON dep.id = d.department_id
            LEFT JOIN users u ON u.id = d.user_id
            """;

    private static final RowMapper<Doctor> MAPPER = (rs, rowNum) -> {
        Doctor doctor = new Doctor();
        doctor.setId(rs.getLong("id"));
        doctor.setName(rs.getString("name"));
        doctor.setSpecialization(rs.getString("specialization"));
        doctor.setEmail(rs.getString("email"));
        doctor.setPhone(rs.getString("phone"));
        long departmentId = rs.getLong("department_id");
        if (!rs.wasNull()) {
            Department department = new Department();
            department.setId(departmentId);
            department.setName(rs.getString("department_name"));
            department.setDescription(rs.getString("department_description"));
            doctor.setDepartment(department);
        }
        long userId = rs.getLong("linked_user_id");
        if (!rs.wasNull()) {
            User user = new User();
            user.setId(userId);
            user.setName(rs.getString("user_name"));
            user.setEmail(rs.getString("user_email"));
            user.setPassword(rs.getString("user_password"));
            doctor.setUser(user);
        }
        return doctor;
    };

    private final JdbcTemplate jdbc;

    public DoctorRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<Doctor> findById(Long id) {
        return jdbc.query(SELECT_SQL + " WHERE d.id = ?", MAPPER, id).stream().findFirst();
    }

    public Optional<Doctor> findByEmail(String email) {
        return jdbc.query(SELECT_SQL + " WHERE d.email = ?", MAPPER, email).stream().findFirst();
    }

    public List<Doctor> findAll() {
        return jdbc.query(SELECT_SQL + " ORDER BY d.id", MAPPER);
    }

    public boolean existsById(Long id) {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM doctors WHERE id = ?", Integer.class, id);
        return count != null && count > 0;
    }

    public void deleteById(Long id) {
        jdbc.update("DELETE FROM doctors WHERE id = ?", id);
    }

    public Doctor save(Doctor doctor) {
        Long departmentId = doctor.getDepartment() == null ? null : doctor.getDepartment().getId();
        Long userId = doctor.getUser() == null ? null : doctor.getUser().getId();
        if (doctor.getId() != null) {
            jdbc.update("""
                            UPDATE doctors
                            SET name = ?, specialization = ?, email = ?, phone = ?, department_id = ?, user_id = ?
                            WHERE id = ?
                            """,
                    doctor.getName(), doctor.getSpecialization(), doctor.getEmail(), doctor.getPhone(),
                    departmentId, userId, doctor.getId());
            return doctor;
        }
        KeyHolder keys = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO doctors (name, specialization, email, phone, department_id, user_id) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, doctor.getName());
            ps.setString(2, doctor.getSpecialization());
            ps.setString(3, doctor.getEmail());
            ps.setString(4, doctor.getPhone());
            if (departmentId == null) {
                ps.setNull(5, Types.BIGINT);
            } else {
                ps.setLong(5, departmentId);
            }
            if (userId == null) {
                ps.setNull(6, Types.BIGINT);
            } else {
                ps.setLong(6, userId);
            }
            return ps;
        }, keys);
        doctor.setId(JdbcKeys.id(keys));
        return doctor;
    }
}

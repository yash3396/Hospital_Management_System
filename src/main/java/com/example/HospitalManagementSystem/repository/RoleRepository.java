package com.example.HospitalManagementSystem.repository;

import com.example.HospitalManagementSystem.model.Role;
import com.example.HospitalManagementSystem.model.RoleName;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
public class RoleRepository {
    private static final RowMapper<Role> ROLE_MAPPER = (rs, rowNum) -> {
        Role role = new Role();
        role.setId(rs.getLong("id"));
        role.setName(RoleName.valueOf(rs.getString("name")));
        return role;
    };

    private final JdbcTemplate jdbc;

    public RoleRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<Role> findByName(RoleName name) {
        return jdbc.query("SELECT id, name FROM roles WHERE name = ?", ROLE_MAPPER, name.name())
                .stream()
                .findFirst();
    }

    public Role save(Role role) {
        if (role.getId() != null) {
            jdbc.update("UPDATE roles SET name = ? WHERE id = ?", role.getName().name(), role.getId());
            return role;
        }
        KeyHolder keys = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO roles (name) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, role.getName().name());
            return ps;
        }, keys);
        role.setId(JdbcKeys.id(keys));
        return role;
    }
}

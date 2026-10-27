package com.example.HospitalManagementSystem.repository;

import com.example.HospitalManagementSystem.model.Role;
import com.example.HospitalManagementSystem.model.RoleName;
import com.example.HospitalManagementSystem.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {
    private static final String USER_JOIN_SQL = """
            SELECT u.id, u.name, u.email, u.password, u.phone, u.address,
                   r.id AS role_id, r.name AS role_name
            FROM users u
            LEFT JOIN user_roles ur ON ur.user_id = u.id
            LEFT JOIN roles r ON r.id = ur.role_id
            """;

    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<User> findById(Long id) {
        return mapUsers(jdbc.query(USER_JOIN_SQL + " WHERE u.id = ?", this::mapJoinRow, id))
                .stream()
                .findFirst();
    }

    public Optional<User> findByEmail(String email) {
        return mapUsers(jdbc.query(USER_JOIN_SQL + " WHERE u.email = ?", this::mapJoinRow, email))
                .stream()
                .findFirst();
    }

    public boolean existsByEmail(String email) {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM users WHERE email = ?", Integer.class, email);
        return count != null && count > 0;
    }

    public List<User> findAll() {
        return mapUsers(jdbc.query(USER_JOIN_SQL + " ORDER BY u.id", this::mapJoinRow));
    }

    public List<User> findByRole(RoleName roleName) {
        return mapUsers(jdbc.query(USER_JOIN_SQL + """
                WHERE u.id IN (
                    SELECT ur2.user_id FROM user_roles ur2
                    JOIN roles r2 ON r2.id = ur2.role_id
                    WHERE r2.name = ?
                )
                ORDER BY u.id
                """, this::mapJoinRow, roleName.name()));
    }

    public User save(User user) {
        if (user.getId() == null) {
            KeyHolder keys = new GeneratedKeyHolder();
            jdbc.update(con -> {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO users (name, email, password, phone, address) VALUES (?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, user.getName());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getPassword());
                ps.setString(4, user.getPhone());
                ps.setString(5, user.getAddress());
                return ps;
            }, keys);
            user.setId(JdbcKeys.id(keys));
        } else {
            jdbc.update(
                    "UPDATE users SET name = ?, email = ?, password = ?, phone = ?, address = ? WHERE id = ?",
                    user.getName(), user.getEmail(), user.getPassword(), user.getPhone(), user.getAddress(), user.getId());
        }
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                if (role.getId() != null) {
                    jdbc.update("INSERT IGNORE INTO user_roles (user_id, role_id) VALUES (?, ?)", user.getId(), role.getId());
                }
            }
        }
        return user;
    }

    private JoinRow mapJoinRow(ResultSet rs, int rowNum) throws SQLException {
        JoinRow row = new JoinRow();
        row.userId = rs.getLong("id");
        row.name = rs.getString("name");
        row.email = rs.getString("email");
        row.password = rs.getString("password");
        row.phone = rs.getString("phone");
        row.address = rs.getString("address");
        long roleId = rs.getLong("role_id");
        if (!rs.wasNull()) {
            row.roleId = roleId;
            row.roleName = rs.getString("role_name");
        }
        return row;
    }

    private List<User> mapUsers(List<JoinRow> rows) {
        Map<Long, User> users = new LinkedHashMap<>();
        for (JoinRow row : rows) {
            User user = users.computeIfAbsent(row.userId, id -> {
                User created = new User();
                created.setId(row.userId);
                created.setName(row.name);
                created.setEmail(row.email);
                created.setPassword(row.password);
                created.setPhone(row.phone);
                created.setAddress(row.address);
                return created;
            });
            if (row.roleId != null && row.roleName != null) {
                Role role = new Role(RoleName.valueOf(row.roleName));
                role.setId(row.roleId);
                user.addRole(role);
            }
        }
        return new ArrayList<>(users.values());
    }

    private static class JoinRow {
        long userId;
        String name;
        String email;
        String password;
        String phone;
        String address;
        Long roleId;
        String roleName;
    }
}

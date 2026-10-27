package com.example.HospitalManagementSystem.repository;

import com.example.HospitalManagementSystem.model.Appointment;
import com.example.HospitalManagementSystem.model.LabTest;
import com.example.HospitalManagementSystem.model.LabTestStatus;
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
public class LabTestRepository {
    private static final String SELECT_SQL = """
            SELECT lt.id, lt.test_name, lt.status, lt.result,
                   a.id AS appointment_id
            FROM lab_tests lt
            JOIN appointments a ON a.id = lt.appointment_id
            """;

    private static final RowMapper<LabTest> MAPPER = (rs, rowNum) -> {
        LabTest test = new LabTest();
        test.setId(rs.getLong("id"));
        test.setTestName(rs.getString("test_name"));
        test.setStatus(LabTestStatus.valueOf(rs.getString("status")));
        test.setResult(rs.getString("result"));
        Appointment appointment = new Appointment();
        appointment.setId(rs.getLong("appointment_id"));
        test.setAppointment(appointment);
        return test;
    };

    private final JdbcTemplate jdbc;

    public LabTestRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<LabTest> findById(Long id) {
        return jdbc.query(SELECT_SQL + " WHERE lt.id = ?", MAPPER, id).stream().findFirst();
    }

    public List<LabTest> findByStatus(LabTestStatus status) {
        return jdbc.query(SELECT_SQL + " WHERE lt.status = ? ORDER BY lt.id", MAPPER, status.name());
    }

    public List<LabTest> findByAppointmentPatientId(Long patientId) {
        return jdbc.query(SELECT_SQL + " WHERE a.patient_id = ? ORDER BY lt.id", MAPPER, patientId);
    }

    public LabTest save(LabTest test) {
        if (test.getId() != null) {
            jdbc.update(
                    "UPDATE lab_tests SET test_name = ?, status = ?, result = ? WHERE id = ?",
                    test.getTestName(), test.getStatus().name(), test.getResult(), test.getId());
            return test;
        }
        KeyHolder keys = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO lab_tests (appointment_id, test_name, status, result) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, test.getAppointment().getId());
            ps.setString(2, test.getTestName());
            ps.setString(3, test.getStatus().name());
            ps.setString(4, test.getResult());
            return ps;
        }, keys);
        test.setId(JdbcKeys.id(keys));
        return test;
    }
}

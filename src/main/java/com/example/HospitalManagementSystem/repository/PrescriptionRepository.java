package com.example.HospitalManagementSystem.repository;

import com.example.HospitalManagementSystem.model.Appointment;
import com.example.HospitalManagementSystem.model.Prescription;
import com.example.HospitalManagementSystem.model.User;
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
public class PrescriptionRepository {
    private static final String SELECT_SQL = """
            SELECT pr.id, pr.diagnosis, pr.medicine, pr.dispensed,
                   a.id AS appointment_id, p.id AS patient_id, p.name AS patient_name
            FROM prescriptions pr
            JOIN appointments a ON a.id = pr.appointment_id
            JOIN users p ON p.id = a.patient_id
            """;

    private static final RowMapper<Prescription> MAPPER = (rs, rowNum) -> {
        Prescription prescription = new Prescription();
        prescription.setId(rs.getLong("id"));
        prescription.setDiagnosis(rs.getString("diagnosis"));
        prescription.setMedicine(rs.getString("medicine"));
        prescription.setDispensed(rs.getBoolean("dispensed"));
        Appointment appointment = new Appointment();
        appointment.setId(rs.getLong("appointment_id"));
        User patient = new User();
        patient.setId(rs.getLong("patient_id"));
        patient.setName(rs.getString("patient_name"));
        appointment.setPatient(patient);
        prescription.setAppointment(appointment);
        return prescription;
    };

    private final JdbcTemplate jdbc;

    public PrescriptionRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<Prescription> findById(Long id) {
        return jdbc.query(SELECT_SQL + " WHERE pr.id = ?", MAPPER, id).stream().findFirst();
    }

    public Optional<Prescription> findByAppointmentId(Long appointmentId) {
        return jdbc.query(SELECT_SQL + " WHERE pr.appointment_id = ?", MAPPER, appointmentId).stream().findFirst();
    }

    public List<Prescription> findByAppointmentPatientId(Long patientId) {
        return jdbc.query(SELECT_SQL + " WHERE a.patient_id = ? ORDER BY pr.id", MAPPER, patientId);
    }

    public List<Prescription> findByDispensedFalse() {
        return jdbc.query(SELECT_SQL + " WHERE pr.dispensed = 0 ORDER BY pr.id", MAPPER);
    }

    public Prescription save(Prescription prescription) {
        boolean dispensed = prescription.isDispensed();
        if (prescription.getId() != null) {
            jdbc.update(
                    "UPDATE prescriptions SET diagnosis = ?, medicine = ?, dispensed = ? WHERE id = ?",
                    prescription.getDiagnosis(), prescription.getMedicine(), dispensed, prescription.getId());
            return prescription;
        }
        KeyHolder keys = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO prescriptions (appointment_id, diagnosis, medicine, dispensed) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, prescription.getAppointment().getId());
            ps.setString(2, prescription.getDiagnosis());
            ps.setString(3, prescription.getMedicine());
            ps.setBoolean(4, dispensed);
            return ps;
        }, keys);
        prescription.setId(JdbcKeys.id(keys));
        return prescription;
    }
}

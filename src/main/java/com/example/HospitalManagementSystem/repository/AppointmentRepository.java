package com.example.HospitalManagementSystem.repository;

import com.example.HospitalManagementSystem.model.Appointment;
import com.example.HospitalManagementSystem.model.AppointmentStatus;
import com.example.HospitalManagementSystem.model.Doctor;
import com.example.HospitalManagementSystem.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class AppointmentRepository {
    private static final String SELECT_SQL = """
            SELECT a.id, a.date, a.slot, a.symptoms, a.status,
                   p.id AS patient_id, p.name AS patient_name, p.email AS patient_email, p.phone AS patient_phone,
                   d.id AS doctor_id, d.name AS doctor_name, d.email AS doctor_email, d.specialization
            FROM appointments a
            JOIN users p ON p.id = a.patient_id
            JOIN doctors d ON d.id = a.doctor_id
            """;

    private static final RowMapper<Appointment> MAPPER = (rs, rowNum) -> {
        Appointment appointment = new Appointment();
        appointment.setId(rs.getLong("id"));
        Date date = rs.getDate("date");
        appointment.setDate(date == null ? null : date.toLocalDate());
        appointment.setSlot(rs.getString("slot"));
        appointment.setSymptoms(rs.getString("symptoms"));
        appointment.setStatus(AppointmentStatus.valueOf(rs.getString("status")));

        User patient = new User();
        patient.setId(rs.getLong("patient_id"));
        patient.setName(rs.getString("patient_name"));
        patient.setEmail(rs.getString("patient_email"));
        patient.setPhone(rs.getString("patient_phone"));
        appointment.setPatient(patient);

        Doctor doctor = new Doctor();
        doctor.setId(rs.getLong("doctor_id"));
        doctor.setName(rs.getString("doctor_name"));
        doctor.setEmail(rs.getString("doctor_email"));
        doctor.setSpecialization(rs.getString("specialization"));
        appointment.setDoctor(doctor);
        return appointment;
    };

    private final JdbcTemplate jdbc;

    public AppointmentRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<Appointment> findById(Long id) {
        return jdbc.query(SELECT_SQL + " WHERE a.id = ?", MAPPER, id).stream().findFirst();
    }

    public List<Appointment> findByDoctorId(Long doctorId) {
        return jdbc.query(SELECT_SQL + " WHERE a.doctor_id = ? ORDER BY a.date, a.slot", MAPPER, doctorId);
    }

    public List<Appointment> findByPatientId(Long patientId) {
        return jdbc.query(SELECT_SQL + " WHERE a.patient_id = ? ORDER BY a.date, a.slot", MAPPER, patientId);
    }

    public List<Appointment> findByDoctorIdAndDate(Long doctorId, LocalDate date) {
        return jdbc.query(SELECT_SQL + " WHERE a.doctor_id = ? AND a.date = ? ORDER BY a.slot", MAPPER, doctorId, date);
    }

    public Appointment save(Appointment appointment) {
        if (appointment.getId() != null) {
            jdbc.update("""
                            UPDATE appointments
                            SET patient_id = ?, doctor_id = ?, date = ?, slot = ?, symptoms = ?, status = ?
                            WHERE id = ?
                            """,
                    appointment.getPatient().getId(),
                    appointment.getDoctor().getId(),
                    appointment.getDate(),
                    appointment.getSlot(),
                    appointment.getSymptoms(),
                    appointment.getStatus().name(),
                    appointment.getId());
            return appointment;
        }
        KeyHolder keys = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO appointments (patient_id, doctor_id, date, slot, symptoms, status) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, appointment.getPatient().getId());
            ps.setLong(2, appointment.getDoctor().getId());
            ps.setDate(3, Date.valueOf(appointment.getDate()));
            ps.setString(4, appointment.getSlot());
            ps.setString(5, appointment.getSymptoms());
            ps.setString(6, appointment.getStatus().name());
            return ps;
        }, keys);
        appointment.setId(JdbcKeys.id(keys));
        return appointment;
    }
}

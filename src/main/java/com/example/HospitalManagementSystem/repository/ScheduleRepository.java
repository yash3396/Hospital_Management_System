package com.example.HospitalManagementSystem.repository;

import com.example.HospitalManagementSystem.model.Doctor;
import com.example.HospitalManagementSystem.model.Schedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ScheduleRepository {
    private static final String SELECT_SQL = """
            SELECT s.id, s.date, s.doctor_id, d.name AS doctor_name, ss.slot
            FROM schedules s
            JOIN doctors d ON d.id = s.doctor_id
            LEFT JOIN schedule_slots ss ON ss.schedule_id = s.id
            """;

    private final JdbcTemplate jdbc;

    public ScheduleRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<Schedule> findByDoctorIdAndDate(Long doctorId, LocalDate date) {
        List<JoinRow> rows = jdbc.query(
                SELECT_SQL + " WHERE s.doctor_id = ? AND s.date = ?",
                this::mapJoinRow,
                doctorId,
                date);
        return mapSchedules(rows).stream().findFirst();
    }

    public void delete(Schedule schedule) {
        if (schedule.getId() != null) {
            jdbc.update("DELETE FROM schedule_slots WHERE schedule_id = ?", schedule.getId());
            jdbc.update("DELETE FROM schedules WHERE id = ?", schedule.getId());
        }
    }

    public Schedule save(Schedule schedule) {
        KeyHolder keys = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO schedules (doctor_id, date) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, schedule.getDoctor().getId());
            ps.setDate(2, Date.valueOf(schedule.getDate()));
            return ps;
        }, keys);
        schedule.setId(JdbcKeys.id(keys));
        if (schedule.getSlots() != null) {
            for (String slot : schedule.getSlots()) {
                jdbc.update("INSERT INTO schedule_slots (schedule_id, slot) VALUES (?, ?)", schedule.getId(), slot);
            }
        }
        return schedule;
    }

    private JoinRow mapJoinRow(ResultSet rs, int rowNum) throws SQLException {
        JoinRow row = new JoinRow();
        row.id = rs.getLong("id");
        Date date = rs.getDate("date");
        row.date = date == null ? null : date.toLocalDate();
        row.doctorId = rs.getLong("doctor_id");
        row.doctorName = rs.getString("doctor_name");
        row.slot = rs.getString("slot");
        return row;
    }

    private List<Schedule> mapSchedules(List<JoinRow> rows) {
        Map<Long, Schedule> schedules = new LinkedHashMap<>();
        for (JoinRow row : rows) {
            Schedule schedule = schedules.computeIfAbsent(row.id, id -> {
                Schedule created = new Schedule();
                created.setId(row.id);
                created.setDate(row.date);
                Doctor doctor = new Doctor();
                doctor.setId(row.doctorId);
                doctor.setName(row.doctorName);
                created.setDoctor(doctor);
                created.setSlots(new ArrayList<>());
                return created;
            });
            if (row.slot != null) {
                schedule.getSlots().add(row.slot);
            }
        }
        return new ArrayList<>(schedules.values());
    }

    private static class JoinRow {
        long id;
        LocalDate date;
        long doctorId;
        String doctorName;
        String slot;
    }
}

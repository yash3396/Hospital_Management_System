package com.example.HospitalManagementSystem.repository;

import com.example.HospitalManagementSystem.model.MedicineStock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
public class MedicineStockRepository {
    private static final RowMapper<MedicineStock> MAPPER = (rs, rowNum) -> {
        MedicineStock stock = new MedicineStock();
        stock.setId(rs.getLong("id"));
        stock.setMedicineName(rs.getString("medicine_name"));
        stock.setQuantity(rs.getInt("quantity"));
        return stock;
    };

    private final JdbcTemplate jdbc;

    public MedicineStockRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<MedicineStock> findByMedicineNameIgnoreCase(String name) {
        return jdbc.query(
                "SELECT id, medicine_name, quantity FROM medicine_stock WHERE LOWER(medicine_name) = LOWER(?)",
                MAPPER,
                name).stream().findFirst();
    }

    public MedicineStock save(MedicineStock stock) {
        if (stock.getId() != null) {
            jdbc.update("UPDATE medicine_stock SET medicine_name = ?, quantity = ? WHERE id = ?",
                    stock.getMedicineName(), stock.getQuantity(), stock.getId());
            return stock;
        }
        KeyHolder keys = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO medicine_stock (medicine_name, quantity) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, stock.getMedicineName());
            ps.setInt(2, stock.getQuantity());
            return ps;
        }, keys);
        stock.setId(JdbcKeys.id(keys));
        return stock;
    }
}

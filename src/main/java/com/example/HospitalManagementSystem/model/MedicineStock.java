package com.example.HospitalManagementSystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "medicine_stock")
public class MedicineStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String medicineName;

    private int quantity;

    public MedicineStock() {}

    public MedicineStock(String medicineName, int quantity) {
        this.medicineName = medicineName;
        this.quantity = quantity;
    }

    public Long getId() { return id; }
    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}

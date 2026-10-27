package com.example.HospitalManagementSystem.model;

public class MedicineStock {
    private Long id;
    private String medicineName;
    private int quantity;

    public MedicineStock() {
    }

    public MedicineStock(String medicineName, int quantity) {
        this.medicineName = medicineName;
        this.quantity = quantity;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}

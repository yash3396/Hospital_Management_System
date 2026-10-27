package com.example.HospitalManagementSystem.model;

public class Doctor {
    private Long id;
    private String name;
    private String specialization;
    private String email;
    private String phone;
    private Department department;
    private User user;

    public Doctor() {
    }

    public Doctor(String name, String specialization, String email, String phone, Department department) {
        this.name = name;
        this.specialization = specialization;
        this.email = email;
        this.phone = phone;
        this.department = department;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}

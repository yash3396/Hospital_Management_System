CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(255),
    address VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS departments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS doctors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    specialization VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(255),
    department_id BIGINT,
    user_id BIGINT UNIQUE,
    CONSTRAINT fk_doctors_department FOREIGN KEY (department_id) REFERENCES departments(id),
    CONSTRAINT fk_doctors_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS schedules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_id BIGINT NOT NULL,
    date DATE NOT NULL,
    UNIQUE KEY uk_schedule_doctor_date (doctor_id, date),
    CONSTRAINT fk_schedules_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);

CREATE TABLE IF NOT EXISTS schedule_slots (
    schedule_id BIGINT NOT NULL,
    slot VARCHAR(255),
    CONSTRAINT fk_schedule_slots_schedule FOREIGN KEY (schedule_id) REFERENCES schedules(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS appointments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    date DATE NOT NULL,
    slot VARCHAR(255) NOT NULL,
    symptoms VARCHAR(1000),
    status VARCHAR(50) NOT NULL,
    UNIQUE KEY uk_appointment_slot (doctor_id, date, slot, patient_id),
    CONSTRAINT fk_appointments_patient FOREIGN KEY (patient_id) REFERENCES users(id),
    CONSTRAINT fk_appointments_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);

CREATE TABLE IF NOT EXISTS prescriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT NOT NULL UNIQUE,
    diagnosis VARCHAR(2000),
    medicine VARCHAR(2000),
    dispensed TINYINT(1) NOT NULL DEFAULT 0,
    CONSTRAINT fk_prescriptions_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(id)
);

CREATE TABLE IF NOT EXISTS lab_tests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT NOT NULL,
    test_name VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    result VARCHAR(4000),
    CONSTRAINT fk_lab_tests_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(id)
);

CREATE TABLE IF NOT EXISTS medicine_stock (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    medicine_name VARCHAR(255) NOT NULL UNIQUE,
    quantity INT NOT NULL DEFAULT 0
);

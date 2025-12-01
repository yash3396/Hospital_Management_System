## HOSPITAL MANAGEMENT SYSTEM
Spring Boot-based Hospital Management System (HMS).
## OVERVIEW
This project provides REST APIs for hospital workflows:
- User authentication (JWT) and role-based access control
- Admin — manage departments, doctors, schedules, users/roles
- Doctor — view appointments, prescriptions
- Patient — register, book appointments
- Receptionist / Lab Technician / Pharmacist modules
## PREREQUISITES
- Java 17+ (match your project)
- Maven or Gradle
- Running database (MySQL/Postgres) configured in application.properties
- Git (optional)
## RUN LOCALLY
1. Configure application.properties (database, JWT secret).
2. Build and run:
    - With Maven:
      mvn clean package
      mvn spring-boot:run
    - Or:
      java -jar target/your-artifact.jar
## API
- Access Swagger UI: http://localhost:8080/swagger-ui.html
**_SCREEN SHOT_**
![Swagger UI Screenshot](images/swagger_hms_1.jpg)
![Swagger UI Screenshot 2](images/swagger_hms_2.jpg)

###   **Authentication:**

- POST /api/auth/register
- POST /api/auth/login
###   **Role-Doctor:**

- GET /api/doctor/appointments
- GET /api/doctor/patients/{patientId}/history
- POST /api/doctor/lab-tests
- POST /api/doctor/prescriptions
###   **Role-Patient:**

- GET /api/patient/lab-results
- GET /api/patient/prescriptions
- POST /api/patient/prescriptions
- GET /api/patient/appointments
###   **Role-Admin:**

- POST /api/admin/departments
- GET /api/admin/departments
- POST /api/admin/doctors
- POST /api/admin/schedules
- DELETE /api/admin/doctors/{doctorId}
- PUT /api/admin/doctors/{doctorId}
- PUT /api/admin/users/{id}/password
- PUT /api/admin/users/{id}/role
###   **Role-LabTechnician:**

- GET /api/lab/tests/pending
- POST /api/lab/results
- PUT /api/lab/tests/{testId}
###   **Role-Pharmacy:**

- GET /api/pharmacy/prescriptions
- POST /api/pharmacy/dispense
- PUT /api/pharmacy/stock
###   **Role-Receptionist:**

- POST /api/receptionist/appointments
- POST /api/receptionist/patients
- PUT /api/receptionist/appointments/{id}/checkin

## IMPLEMENATION OF ENDPOINTS DETAILS :
1. start the application
2. create a ADMIN user using register endpoint
   POST /api/auth/register Payload (note: RegistrationRequest.role must match enum name — e.g. "ROLE_ADMIN"):
**{
"name": "AdminUser",
"email": "admin@example.com", 
"password": "AdminPass123!",
"role": "ROLE_ADMIN" 
}** 
3. Login as ADMIN to obtain JWT
   POST /api/auth/login Payload: 
**{ 
"email": "admin@example.com",
"password": "AdminPass123!"
}**
   •	Response: { "token": "<JWT>" }
   •	Use that token for Authorization header in subsequent admin requests: Authorization: Bearer <JWT>
4.	Create departments (ADMIN)
      POST /api/admin/departments Payload: 
**{
"name": "Neurology",
"description": "nerve related treatments" 
}**
      •	Response: 201 Created with Department entity.
![Department Creation Screenshot 1](images/swagger_hms_adding_departments_1.jpg)
![Department Creation Screenshot 2](images/swagger_hms_adding_departments_mysql.jpg)
5.	(ADMIN) Add a doctor — this will also create (or reuse) a linked User with ROLE_DOCTOR
      POST /api/admin/doctors Payload: 
**{
"name": "Dr karthiga",
"specialization": "Neurology",
"email": "karthiga.doctor@example.com",
"phone": "1234567890",
"departmentId": 2 
}**
      •	Response: 201 Created with Doctor entity (saved doctor.user created or linked)
      •	Note: AdminServiceImpl generates a random password for new doctor accounts (not returned by default). Consider emailing a password reset link or returning it in a secure way if desired.
![Doctor Creation Screenshot 1](images/swagger_hms_adding_doctors_1.jpg)
![Doctor Creation Screenshot 2](images/swagger_hms_adding_doctors_mysql.jpg)
6.	Login as Doctor after resetting the password (if you have the plain password or after setting doctor password)
      POST /api/auth/login 
**{
"email": "karthiga.doctor@example.com",
"password": "theGeneratedOrSetPassword"
}**
      •	Response: token — use for doctor endpoints.
7.	Create a Patient
      •	Option A: Patient self-register POST /api/auth/register 
**{ 
"name": "John Patient",
"email": "john.patient@example.com",
"password": "PatientPass123",
"role": "ROLE_PATIENT" 
}**
![Patient Registration Screenshot 1](images/swagger_hms_adding_patients_1.jpg)
![Patient Registration Screenshot 2](images/swagger_hms_adding_patients_mysql.jpg)
      •	Option B: Receptionist creates patient (requires RECEPTIONIST token): POST /api/receptionist/patients 
**{ 
"name":"John Doe",
"email":"john.doe@example.com", 
"password":"TempPass123",
"phone":"9876543210" 
}**
8.	Patient login to obtain token
      POST /api/auth/login 
**{
"email": "john.patient@example.com",
"password": "PatientPass123" 
}**
      •	Response: token — used for patient endpoints.
![Patient Login Screenshot 1](images/swagger_hms_patient_login.jpg)
9.	Patient books appointment (PATIENT)
      POST /api/patient/appointments Authorization: Bearer <patient-token> Payload: 
**{
"doctorId": 1,
"date": "2025-12-01",
"slot": "10:00",
"symptoms": "Headache and fever" 
}**
      •	Response: 200 OK with appointment details.
![Appointment Booking Screenshot 1](images/swagger_hms_doctor_appointment.jpg)
10.	Doctor views appointments (DOCTOR)
       GET /api/doctor/appointments Authorization: Bearer <doctor-token>
![Doctor Appointments Screenshot 1](images/swagger_hms_doctor_viewing_appointments.jpg)
11.	Doctor adds/updates prescription for an appointment (DOCTOR)
       POST /api/doctor/prescriptions Authorization: Bearer <doctor-token> 
**{
"appointmentId": 12,
"diagnosis": "Fever",
"medicine": "Paracetamol 500mg" 
}**
![Doctor Prescription Screenshot 1](images/swagger_hms_doctor_prescriptions.jpg)
12.	Doctor requests lab test (DOCTOR)
       POST /api/doctor/lab-tests Authorization: Bearer <doctor-token> 
**{
"appointmentId": 12,
"testName": "Blood Test" 
}**
![Doctor Lab Test Request Screenshot 1](images/swagger_hms_requesting_lab_tests.jpg)
13.	Lab technician views pending tests and adds results (LAB_TECHNICIAN)
       GET /api/lab/tests/pending
       PUT /api/lab/tests/{testId} (update status)
       Give request body as any one of the below:
       **{ "status": "COMPLETED" }
       { "status": "IN_PROGRESS" }
       { "status": "CANCELLED" }**
![Lab Technician Screenshot 1](images/swagger_hms_lab_tech_status.jpg)
       •	POST /api/lab/results (add result)
       Give Request body like :
       **{
         "testRequestId": 1,
         "result": "Normal blood count — no abnormalities" 
       }**
![Lab Technician Screenshot 1](images/swagger_hms_post_the_test_results.jpg)
14.	Pharmacist dispenses medicines (PHARMACIST)
       GET /api/pharmacy/prescriptions (list undispensed)
       POST /api/pharmacy/dispense 
**{ 
"prescriptionId": 20 
}**
![Pharmacist Dispense Screenshot 1](images/swagger_hms_pharma_dispensed.jpg)
       PUT /api/pharmacy/stock 
**{ 
"medicineName": "Paracetamol", 
"quantity": 100
}**
![Pharmacist Stock Update Screenshot 1](images/swagger_hms_pharma_stock.jpg)
15.	Receptionist check-in flow
       POST /api/receptionist/appointments?patientId=5
       PUT /api/receptionist/appointments/{id}/checkin **{ "status": "CHECKED_IN" }**
## DATABASE STRUCTURE IN MYSQL WORKBENCH
![Database Structure Screenshot 1](images/swagger_hms_database_structure.jpg)

## users table
![Users Table Screenshot 1](images/swagger_hms_mysql_users_table.jpg)
## user_roles table
![User Roles Table Screenshot 1](images/swagger_hms_mysql_user_roles_table.jpg)
## schedules table
![Schedules Table Screenshot 1](images/swagger_hms_mysql_schedules_table.jpg)
## schedule_slots table
![Schedule Slots Table Screenshot 1](images/swagger_hms_mysql_schedule_slots_table.jpg)
## roles table
![Roles Table Screenshot 1](images/swagger_hms_mysql_roles_table.jpg)
## prescriptions table
![Prescriptions Table Screenshot 1](images/swagger_hms_mysql_prescriptions_table.jpg)
## medicine_stock table
![Medicine Stock Table Screenshot 1](images/swagger_hms_mysql_medicine_stock_table.jpg)
## lab_tests table
![Lab Tests Table Screenshot 1](images/swagger_hms_mysql_lab_tests_table.jpg)
## appointments table
![Appointments Table Screenshot 1](images/swagger_hms_mysql_appointments_table.jpg)
## departments table
![Departments Table Screenshot 1](images/swagger_hms_mysql_departments_table.jpg)
## doctors table
![Doctors Table Screenshot 1](images/swagger_hms_mysql_doctors_table.jpg)

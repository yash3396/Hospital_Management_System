package com.example.HospitalManagementSystem.service;

import com.example.HospitalManagementSystem.view.*;
import com.example.HospitalManagementSystem.model.*;
import com.example.HospitalManagementSystem.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class HospitalServiceImpl implements HospitalService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final LabTestRepository labTestRepository;
    private final MedicineStockRepository medicineStockRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public HospitalServiceImpl(AppointmentRepository appointmentRepository,
                               DoctorRepository doctorRepository,
                               UserRepository userRepository,
                               PrescriptionRepository prescriptionRepository,
                               LabTestRepository labTestRepository,
                               MedicineStockRepository medicineStockRepository,
                               PasswordEncoder passwordEncoder,
                               RoleRepository roleRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.labTestRepository = labTestRepository;
        this.medicineStockRepository = medicineStockRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    // ------------------ Doctor -------------------
    @Override
    public List<Appointment> getDoctorAppointmentsByEmail(String doctorEmail) {
        return doctorRepository.findByEmail(doctorEmail)
                .map(d -> appointmentRepository.findByDoctorId(d.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found for email: " + doctorEmail));
    }

    @Override
    @Transactional
    public Prescription createOrUpdatePrescription(PrescriptionRequest req, String doctorEmail) {
        Appointment appt = appointmentRepository.findById(req.getAppointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + req.getAppointmentId()));

        // ensure the authenticated doctor owns the appointment
        Doctor doc = doctorRepository.findByEmail(doctorEmail).orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + doctorEmail));
        if (!appt.getDoctor().getId().equals(doc.getId())) {
            throw new IllegalArgumentException("Doctor does not own this appointment");
        }

        // if prescription exists for appointment, update; otherwise create
        Prescription p = prescriptionRepository.findByAppointmentId(appt.getId())
                .orElse(new Prescription(appt, req.getDiagnosis(), req.getMedicine()));

        p.setDiagnosis(req.getDiagnosis());
        p.setMedicine(req.getMedicine());
        return prescriptionRepository.save(p);
    }

    @Override
    @Transactional
    public LabTest requestLabTest(LabTestRequest req, String doctorEmail) {
        Appointment appt = appointmentRepository.findById(req.getAppointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + req.getAppointmentId()));

        // ensure doctor owns appointment
        Doctor doc = doctorRepository.findByEmail(doctorEmail).orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + doctorEmail));
        if (!appt.getDoctor().getId().equals(doc.getId())) {
            throw new IllegalArgumentException("Doctor does not own this appointment");
        }

        LabTest lt = new LabTest(appt, req.getTestName());
        return labTestRepository.save(lt);
    }

    @Override
    public List<Appointment> getPatientHistory(Long patientId, String doctorEmail) {
        // doctor must exist (we don't strictly enforce ownership for viewing history in this simple impl)
        doctorRepository.findByEmail(doctorEmail).orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + doctorEmail));
        return appointmentRepository.findByPatientId(patientId);
    }

    // ------------------ Patient -------------------
    @Override
    @Transactional
    public Appointment bookAppointment(String patientEmail, AppointmentBookingRequest req) {
        com.example.HospitalManagementSystem.model.User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new IllegalArgumentException("Patient user not found: " + patientEmail));
        Doctor doctor = doctorRepository.findById(req.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + req.getDoctorId()));
        LocalDate date = LocalDate.parse(req.getDate());
        Appointment appt = new Appointment(patient, doctor, date, req.getSlot(), req.getSymptoms());
        return appointmentRepository.save(appt);
    }

    @Override
    public List<Appointment> getPatientAppointments(String patientEmail) {
        com.example.HospitalManagementSystem.model.User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new IllegalArgumentException("Patient user not found: " + patientEmail));
        return appointmentRepository.findByPatientId(patient.getId());
    }

    @Override
    public List<Prescription> getPatientPrescriptions(String patientEmail) {
        com.example.HospitalManagementSystem.model.User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new IllegalArgumentException("Patient user not found: " + patientEmail));
        return prescriptionRepository.findByAppointmentPatientId(patient.getId());
    }

    @Override
    public List<LabTest> getPatientLabResults(String patientEmail) {
        com.example.HospitalManagementSystem.model.User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new IllegalArgumentException("Patient user not found: " + patientEmail));
        return labTestRepository.findByAppointmentPatientId(patient.getId());
    }

    // ------------------ Receptionist -------------------
    @Override
    @Transactional
    public com.example.HospitalManagementSystem.model.User registerPatientByReceptionist(ReceptionistPatientRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + req.getEmail());
        }
        com.example.HospitalManagementSystem.model.User u = new com.example.HospitalManagementSystem.model.User(req.getName(), req.getEmail(), passwordEncoder.encode(req.getPassword()));
        u.setPhone(req.getPhone());
        u.setAddress(req.getAddress());
        // assign ROLE_PATIENT
        Role role = roleRepository.findByName(RoleName.ROLE_PATIENT).orElseGet(() -> roleRepository.save(new Role(RoleName.ROLE_PATIENT)));
        u.addRole(role);
        return userRepository.save(u);
    }

    @Override
    @Transactional
    public Appointment bookAppointmentForPatientByReceptionist(Long patientId, AppointmentBookingRequest req) {
        com.example.HospitalManagementSystem.model.User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + patientId));
        Doctor doctor = doctorRepository.findById(req.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + req.getDoctorId()));
        LocalDate date = LocalDate.parse(req.getDate());
        Appointment appt = new Appointment(patient, doctor, date, req.getSlot(), null);
        return appointmentRepository.save(appt);
    }

    @Override
    @Transactional
    public Appointment checkinAppointment(Long appointmentId, String status) {
        Appointment appt = appointmentRepository.findById(appointmentId).orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + appointmentId));
        try {
            appt.setStatus(AppointmentStatus.valueOf(status));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
        return appointmentRepository.save(appt);
    }

    // ------------------ Lab Technician -------------------
    @Override
    @Transactional
    public LabTest addLabResult(Long testRequestId, String result) {
        LabTest t = labTestRepository.findById(testRequestId).orElseThrow(() -> new IllegalArgumentException("Test not found: " + testRequestId));
        t.setResult(result);
        t.setStatus(LabTestStatus.COMPLETED);
        return labTestRepository.save(t);
    }

    @Override
    @Transactional
    public LabTest updateLabTestStatus(Long testId, String status) {
        LabTest t = labTestRepository.findById(testId).orElseThrow(() -> new IllegalArgumentException("Test not found: " + testId));
        try {
            t.setStatus(LabTestStatus.valueOf(status));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid lab test status: " + status);
        }
        return labTestRepository.save(t);
    }

    @Override
    public List<LabTest> getPendingLabTests() {
        return labTestRepository.findByStatus(LabTestStatus.PENDING);
    }

    // ------------------ Pharmacy -------------------
    @Override
    @Transactional
    public Prescription dispenseMedicine(Long prescriptionId) {
        Prescription p = prescriptionRepository.findById(prescriptionId).orElseThrow(() -> new IllegalArgumentException("Prescription not found: " + prescriptionId));
        p.setDispensed(true);
        String medName = p.getMedicine();
        if (medName != null && !medName.isBlank()) {
            medicineStockRepository.findByMedicineNameIgnoreCase(medName)
                    .ifPresent(ms -> {
                        ms.setQuantity(Math.max(0, ms.getQuantity() - 1));
                        medicineStockRepository.save(ms);
                    });
        }
        return prescriptionRepository.save(p);
    }

    @Override
    @Transactional
    public MedicineStock updateMedicineStock(StockUpdateRequest req) {
        MedicineStock ms = medicineStockRepository.findByMedicineNameIgnoreCase(req.getMedicineName())
                .orElseGet(() -> new MedicineStock(req.getMedicineName(), 0));
        ms.setQuantity(req.getQuantity());
        return medicineStockRepository.save(ms);
    }

    @Override
    public List<Prescription> getPrescriptionsToDispense() {
        return prescriptionRepository.findByDispensedFalse();
    }

    @Override
    public List<Doctor> listDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public List<User> listPatients() {
        return userRepository.findByRole(RoleName.ROLE_PATIENT);
    }

}

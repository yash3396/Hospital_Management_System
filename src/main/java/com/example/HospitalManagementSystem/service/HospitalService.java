package com.example.HospitalManagementSystem.service;

import com.example.HospitalManagementSystem.dto.*;
import com.example.HospitalManagementSystem.model.Appointment;
import com.example.HospitalManagementSystem.model.LabTest;
import com.example.HospitalManagementSystem.model.MedicineStock;
import com.example.HospitalManagementSystem.model.Prescription;

import java.util.List;

public interface HospitalService {
    List<Appointment> getDoctorAppointmentsByEmail(String doctorEmail);
    Prescription createOrUpdatePrescription(PrescriptionRequest req, String doctorEmail);
    LabTest requestLabTest(LabTestRequest req, String doctorEmail);
    List<Appointment> getPatientHistory(Long patientId, String doctorEmail);

    // patient
    Appointment bookAppointment(String patientEmail, AppointmentBookingRequest req);
    List<Appointment> getPatientAppointments(String patientEmail);
    List<Prescription> getPatientPrescriptions(String patientEmail);
    List<LabTest> getPatientLabResults(String patientEmail);

    // receptionist
    com.example.HospitalManagementSystem.model.User registerPatientByReceptionist(ReceptionistPatientRequest req);
    Appointment bookAppointmentForPatientByReceptionist(Long patientId, AppointmentBookingRequest req);
    Appointment checkinAppointment(Long appointmentId, String status);

    // lab technician
    LabTest addLabResult(Long testRequestId, String result);
    LabTest updateLabTestStatus(Long testId, String status);
    List<LabTest> getPendingLabTests();

    // pharmacy
    Prescription dispenseMedicine(Long prescriptionId);
    MedicineStock updateMedicineStock(StockUpdateRequest req);
    List<Prescription> getPrescriptionsToDispense();
}

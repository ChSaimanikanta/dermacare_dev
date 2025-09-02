package com.clinicadmin.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.clinicadmin.dto.Address;
import com.clinicadmin.dto.BankAccountDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "lab_technicians")
public class LabTechnicianEntity {

    @Id
    
    private String id;   // Custom ID like LT_A27873EB
private String clinicId;
    private String fullName;
    private String gender;

    private  String role = "LAB TECHNICIAN";
    private String dateOfBirth;

    @Indexed(unique = true)
    private String contactNumber;   // This will also be username
    private String governmentId;
    private String qualificationOrCertifications;
    private String dateOfJoining;
    private String departmentOrAssignedLab;
    private String yearOfExperience;
    private String specialization;
    private String shiftTimingsOrAvailability;
    private Address address;

    private String emergencyContact;

    private BankAccountDetails bankAccountDetails; // 👈 embed bank account

    private String medicalFitnessCertificate;

    private String userName;  // auto = contactNumber
    private String password;  // auto-generate

    // Optional Fields
    private String emailId;
    private String labLicenseOrRegistration;
    private String vaccinationStatus;
    private String previousEmploymentHistory;

}
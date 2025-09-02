package com.clinicadmin.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.clinicadmin.dto.SecurityStaffDTO;
import com.clinicadmin.entity.SecurityStaff;

public class SecurityStaffMapper {

    // Encode a string to Base64
    public static String encode(String input) {
        if (input == null) return null;
        return Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }

    // Decode a Base64 string back to plain text
    public static String decode(String input) {
        if (input == null) return null;
        return new String(Base64.getDecoder().decode(input), StandardCharsets.UTF_8);
    }

    // DTO → Entity (encode sensitive fields before saving)
    public static SecurityStaff toEntity(SecurityStaffDTO dto) {
        SecurityStaff staff = new SecurityStaff();

        staff.setSecurityStaffId(dto.getSecurityStaffId());
        staff.setClinicId(dto.getClinicId());
        staff.setFullName(dto.getFullName());
        staff.setDateOfBirth(dto.getDateOfBirth());
        staff.setGender(dto.getGender());
        staff.setContactNumber(dto.getContactNumber());
        staff.setGovermentId(dto.getGovermentId());
        staff.setDateOfJoining(dto.getDateOfJoining());
        staff.setDepartment(dto.getDepartment());
        staff.setAddress(dto.getAddress());
        staff.setBankAccountDetails(dto.getBankAccountDetails());

        // Encode certificates
        staff.setPoliceVerification(encode(dto.getPoliceVerification()));
        staff.setMedicalFitnessCertificate(encode(dto.getMedicalFitnessCertificate()));

        staff.setEmailId(dto.getEmailId());
        staff.setTraningOrGuardLicense(dto.getTraningOrGuardLicense());
        staff.setPreviousEmployeeHistory(dto.getPreviousEmployeeHistory());

        return staff;
    }

    // Entity → DTO (decode sensitive fields before returning)
    public static SecurityStaffDTO toDTO(SecurityStaff staff) {
        SecurityStaffDTO dto = new SecurityStaffDTO();

        dto.setSecurityStaffId(staff.getSecurityStaffId());
        dto.setClinicId(staff.getClinicId());
        dto.setFullName(staff.getFullName());
        dto.setDateOfBirth(staff.getDateOfBirth());
        dto.setGender(staff.getGender());
        dto.setContactNumber(staff.getContactNumber());
        dto.setGovermentId(staff.getGovermentId());
        dto.setDateOfJoining(staff.getDateOfJoining());
        dto.setDepartment(staff.getDepartment());
        dto.setAddress(staff.getAddress());
        dto.setBankAccountDetails(staff.getBankAccountDetails());

        // Decode certificates
        dto.setPoliceVerification(decode(staff.getPoliceVerification()));
        dto.setMedicalFitnessCertificate(decode(staff.getMedicalFitnessCertificate()));

        dto.setEmailId(staff.getEmailId());
        dto.setTraningOrGuardLicense(staff.getTraningOrGuardLicense());
        dto.setPreviousEmployeeHistory(staff.getPreviousEmployeeHistory());

        return dto;
    }
}
package com.clinicadmin.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.clinicadmin.dto.SecurityStaffDTO;
import com.clinicadmin.entity.SecurityStaff;

public class SecurityStaffMapper {

    // Encode to Base64 (used when saving images/PDFs to DB)
    private static String encodeIfNotBase64(String input) {
        if (input == null || input.isBlank()) return input;

        // Check if already Base64
        String base64Pattern = "^[A-Za-z0-9+/]*={0,2}$";
        if (input.matches(base64Pattern) && input.length() % 4 == 0) {
            try {
                Base64.getDecoder().decode(input); // valid Base64
                return input; // already Base64, return as is
            } catch (IllegalArgumentException e) {
                // not Base64, so encode
            }
        }
        return Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }

    // Always return safe Base64 for frontend
    private static String safeReturnAsBase64(String input) {
        if (input == null) return null;
        try {
            Base64.getDecoder().decode(input); // valid Base64
            return input; // already Base64
        } catch (Exception e) {
            return Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
        }
    }

    // DTO → Entity (encode before saving)
    public static SecurityStaff toEntity(SecurityStaffDTO dto) {
        if (dto == null) return null;

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
        staff.setPermissions(dto.getPermissions());

        // Encode certificates/images
        staff.setPoliceVerification(encodeIfNotBase64(dto.getPoliceVerification()));
        staff.setMedicalFitnessCertificate(encodeIfNotBase64(dto.getMedicalFitnessCertificate()));
        staff.setProfilePicture(encodeIfNotBase64(dto.getProfilePicture()));

        staff.setEmailId(dto.getEmailId());
        staff.setTraningOrGuardLicense(dto.getTraningOrGuardLicense());
        staff.setPreviousEmployeeHistory(dto.getPreviousEmployeeHistory());

        // ⚡ IMPORTANT:
        // Do NOT set username/password here, they will be generated in ServiceImpl
        return staff;
    }

    // Entity → DTO (return Base64 for frontend rendering)
    public static SecurityStaffDTO toDTO(SecurityStaff staff) {
        if (staff == null) return null;

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
        dto.setPermissions(staff.getPermissions());

        dto.setPoliceVerification(safeReturnAsBase64(staff.getPoliceVerification()));
        dto.setMedicalFitnessCertificate(safeReturnAsBase64(staff.getMedicalFitnessCertificate()));
        dto.setProfilePicture(safeReturnAsBase64(staff.getProfilePicture()));

        dto.setEmailId(staff.getEmailId());
        dto.setTraningOrGuardLicense(staff.getTraningOrGuardLicense());
        dto.setPreviousEmployeeHistory(staff.getPreviousEmployeeHistory());

        // Include these in DTO for login/admin use
        dto.setUserName(staff.getUserName());
        dto.setRole(staff.getRole());
        dto.setPassword(staff.getPassword()); // word back to frontend

        return dto;
    }
}

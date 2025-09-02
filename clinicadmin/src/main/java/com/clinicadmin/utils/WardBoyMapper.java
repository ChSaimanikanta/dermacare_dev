package com.clinicadmin.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.clinicadmin.dto.WardBoyDTO;
import com.clinicadmin.entity.WardBoy;

public class WardBoyMapper {

    private static String encodeIfNotBase64(String input) {
        if (input == null || input.isBlank()) return input;

        String base64Pattern = "^[A-Za-z0-9+/]*={0,2}$";
        if (input.matches(base64Pattern) && input.length() % 4 == 0) {
            try {
                Base64.getDecoder().decode(input);
                return input; // Already Base64
            } catch (IllegalArgumentException e) { }
        }
        return Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }

    private static String decodeIfBase64(String input) {
        if (input == null || input.isBlank()) return input;
        try {
            Base64.getDecoder().decode(input);
            return input; 
        } catch (IllegalArgumentException e) {
            return input; 
        }
    }

    public static WardBoy toEntity(WardBoyDTO dto) {
        if (dto == null) return null;

        WardBoy wardBoy = new WardBoy();
        wardBoy.setWardBoyId(dto.getWardBoyId());
        wardBoy.setFullName(dto.getFullName());
        wardBoy.setClinicId(dto.getClinicId());
        wardBoy.setDateOfBirth(dto.getDateOfBirth());
        wardBoy.setContactNumber(dto.getContactNumber());
        wardBoy.setGovernmentId(dto.getGovernmentId());
        wardBoy.setDateOfJoining(dto.getDateOfJoining());
        wardBoy.setDepartment(dto.getDepartment());
        wardBoy.setBankAccountDetails(dto.getBankAccountDetails());
        wardBoy.setAddress(dto.getAddress());
        wardBoy.setGender(dto.getGender());
        wardBoy.setWorkExprience(dto.getWorkExprience());
        wardBoy.setShiftTimingOrAvailability(dto.getShiftTimingOrAvailability());
        wardBoy.setEmergencyContact(dto.getEmergencyContact());

        

        wardBoy.setMedicalFitnessCertificate(encodeIfNotBase64(dto.getMedicalFitnessCertificate()));
        wardBoy.setBasicHealthFirstAidTrainingCertificate(encodeIfNotBase64(dto.getBasicHealthFirstAidTrainingCertificate()));
        wardBoy.setPoliceVerification(encodeIfNotBase64(dto.getPoliceVerification()));
        wardBoy.setEmailId(dto.getEmailId());
        wardBoy.setPreviousEmploymentHistory(dto.getPreviousEmploymentHistory());
        wardBoy.setRole(dto.getRole() != null ? dto.getRole() : "WARD_BOY");


        return wardBoy;
    }

    public static WardBoyDTO toDTO(WardBoy entity) {
        if (entity == null) return null;

        WardBoyDTO dto = new WardBoyDTO();
        dto.setWardBoyId(entity.getWardBoyId());
        dto.setFullName(entity.getFullName());
        dto.setClinicId(entity.getClinicId());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setContactNumber(entity.getContactNumber());
        dto.setGovernmentId(entity.getGovernmentId());
        dto.setDateOfJoining(entity.getDateOfJoining());
        dto.setDepartment(entity.getDepartment());

        dto.setBankAccountDetails(entity.getBankAccountDetails());
        dto.setMedicalFitnessCertificate(decodeIfBase64(entity.getMedicalFitnessCertificate()));
        dto.setBasicHealthFirstAidTrainingCertificate(decodeIfBase64(entity.getBasicHealthFirstAidTrainingCertificate()));
        dto.setPoliceVerification(decodeIfBase64(entity.getPoliceVerification()));
        dto.setEmailId(entity.getEmailId());
        dto.setPreviousEmploymentHistory(entity.getPreviousEmploymentHistory());
        dto.setRole(entity.getRole());


        return dto;
    }
}
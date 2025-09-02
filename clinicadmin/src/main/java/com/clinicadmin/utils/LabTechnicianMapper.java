package com.clinicadmin.utils;



import java.util.Base64;

import com.clinicadmin.dto.LabTechnicianRequestDTO;
import com.clinicadmin.entity.LabTechnicianEntity;

public class LabTechnicianMapper {

    // Convert RequestDTO -> Entity (encode files to Base64)
    public static LabTechnicianEntity toEntity(LabTechnicianRequestDTO dto) {
        LabTechnicianEntity entity = new LabTechnicianEntity();
        entity.setFullName(dto.getFullName());
        entity.setClinicId(dto.getClinicId());
        entity.setGender(dto.getGender());
        entity.setDateOfBirth(dto.getDateOfBirth());
        entity.setContactNumber(dto.getContactNumber());
        entity.setGovernmentId(dto.getGovernmentId());

        // Encode fields if not null
        if (dto.getQualificationOrCertifications() != null) {
            entity.setQualificationOrCertifications(
                Base64.getEncoder().encodeToString(dto.getQualificationOrCertifications().getBytes())
            );
        }

        entity.setDateOfJoining(dto.getDateOfJoining());
        entity.setDepartmentOrAssignedLab(dto.getDepartmentOrAssignedLab());
        entity.setYearOfExperience(dto.getYearOfExperience());
        entity.setSpecialization(dto.getSpecialization());
        entity.setShiftTimingsOrAvailability(dto.getShiftTimingsOrAvailability());
        entity.setAddress(dto.getAddress());
        entity.setEmergencyContact(dto.getEmergencyContact());
        entity.setBankAccountDetails(dto.getBankAccountDetails());

        if (dto.getMedicalFitnessCertificate() != null) {
            entity.setMedicalFitnessCertificate(
                Base64.getEncoder().encodeToString(dto.getMedicalFitnessCertificate().getBytes())
            );
        }

        entity.setEmailId(dto.getEmailId());
        entity.setLabLicenseOrRegistration(dto.getLabLicenseOrRegistration());
        entity.setVaccinationStatus(dto.getVaccinationStatus());
        entity.setPreviousEmploymentHistory(dto.getPreviousEmploymentHistory());
        return entity;
    }

    // Convert Entity -> ResponseDTO (decode Base64 back to string)
    public static LabTechnicianRequestDTO toResponseDTO(LabTechnicianEntity entity) {
    	LabTechnicianRequestDTO dto = new LabTechnicianRequestDTO();
        dto.setId(entity.getId());
        dto.setFullName(entity.getFullName());
        dto.setGender(entity.getGender());
        dto.setRole(entity.getRole());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setContactNumber(entity.getContactNumber());
        dto.setGovernmentId(entity.getGovernmentId());

        // Decode fields if not null
        if (entity.getQualificationOrCertifications() != null) {
            dto.setQualificationOrCertifications(
                new String(Base64.getDecoder().decode(entity.getQualificationOrCertifications()))
            );
        }

        dto.setDateOfJoining(entity.getDateOfJoining());
        dto.setClinicId(entity.getClinicId());
        dto.setDepartmentOrAssignedLab(entity.getDepartmentOrAssignedLab());
        dto.setYearOfExperience(entity.getYearOfExperience());
        dto.setSpecialization(entity.getSpecialization());
        dto.setShiftTimingsOrAvailability(entity.getShiftTimingsOrAvailability());
        dto.setAddress(entity.getAddress());
        dto.setEmergencyContact(entity.getEmergencyContact());
        dto.setBankAccountDetails(entity.getBankAccountDetails());

        if (entity.getMedicalFitnessCertificate() != null) {
            dto.setMedicalFitnessCertificate(
                new String(Base64.getDecoder().decode(entity.getMedicalFitnessCertificate()))
            );
        }

        dto.setUserName(entity.getUserName());
        dto.setEmailId(entity.getEmailId());
        dto.setLabLicenseOrRegistration(entity.getLabLicenseOrRegistration());
        dto.setVaccinationStatus(entity.getVaccinationStatus());
        dto.setPreviousEmploymentHistory(entity.getPreviousEmploymentHistory());
        return dto;
    }
}
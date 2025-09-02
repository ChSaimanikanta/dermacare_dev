package com.clinicadmin.utils;

import com.clinicadmin.dto.ReceptionistRequestDTO;
import com.clinicadmin.entity.ReceptionistEntity;

public class ReceptionistMapper {

    // Convert DTO -> Entity (for CREATE)
    public static ReceptionistEntity toEntity(ReceptionistRequestDTO dto) {
        if (dto == null) return null;

        ReceptionistEntity entity = new ReceptionistEntity();
        applyDtoToEntity(dto, entity); // reuse common method
        return entity;
    }

    // Convert Entity -> DTO
    public static ReceptionistRequestDTO toDTO(ReceptionistEntity entity) {
        if (entity == null) return null;

        ReceptionistRequestDTO dto = new ReceptionistRequestDTO();
        dto.setId(entity.getId());
        dto.setClinicId(entity.getClinicId());
        dto.setRole(entity.getRole());
        dto.setAddress(entity.getAddress());
        dto.setEmergencyContact(entity.getEmergencyContact());
        dto.setUserName(entity.getUserName());
        dto.setPassword(entity.getPassword());
        dto.setFullName(entity.getFullName());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setContactNumber(entity.getContactNumber());
        dto.setQualification(entity.getQualification());
        dto.setGovernmentId(entity.getGovernmentId());
        dto.setDateOfJoining(entity.getDateOfJoining());
        dto.setDepartment(entity.getDepartment());
        dto.setBankAccountDetails(entity.getBankAccountDetails());
        dto.setEmailId(entity.getEmailId());
        dto.setGraduationCertificate(entity.getGraduationCertificate());
        dto.setComputerSkillsProof(entity.getComputerSkillsProof());
        dto.setPreviousEmploymentHistory(entity.getPreviousEmploymentHistory());
        return dto;
    }

    // ✅ New: Update only non-null fields from DTO to existing Entity
    public static void updateEntityFromDto(ReceptionistRequestDTO dto, ReceptionistEntity entity) {
        if (dto == null || entity == null) return;

        if (dto.getClinicId() != null) entity.setClinicId(dto.getClinicId());
        if (dto.getRole() != null) entity.setRole(dto.getRole());
        if (dto.getAddress() != null) entity.setAddress(dto.getAddress());
        if (dto.getEmergencyContact() != null) entity.setEmergencyContact(dto.getEmergencyContact());
        if (dto.getUserName() != null) entity.setUserName(dto.getUserName());
        if (dto.getPassword() != null) entity.setPassword(dto.getPassword());
        if (dto.getFullName() != null) entity.setFullName(dto.getFullName());
        if (dto.getDateOfBirth() != null) entity.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getContactNumber() != null) entity.setContactNumber(dto.getContactNumber());
        if (dto.getQualification() != null) entity.setQualification(dto.getQualification());
        if (dto.getGovernmentId() != null) entity.setGovernmentId(dto.getGovernmentId());
        if (dto.getDateOfJoining() != null) entity.setDateOfJoining(dto.getDateOfJoining());
        if (dto.getDepartment() != null) entity.setDepartment(dto.getDepartment());
        if (dto.getBankAccountDetails() != null) entity.setBankAccountDetails(dto.getBankAccountDetails());
        if (dto.getEmailId() != null) entity.setEmailId(dto.getEmailId());
        if (dto.getGraduationCertificate() != null) entity.setGraduationCertificate(dto.getGraduationCertificate());
        if (dto.getComputerSkillsProof() != null) entity.setComputerSkillsProof(dto.getComputerSkillsProof());
        if (dto.getPreviousEmploymentHistory() != null) entity.setPreviousEmploymentHistory(dto.getPreviousEmploymentHistory());
    }

    // Helper for Create
    private static void applyDtoToEntity(ReceptionistRequestDTO dto, ReceptionistEntity entity) {
        entity.setId(dto.getId());
        entity.setClinicId(dto.getClinicId());
        entity.setRole(dto.getRole() != null ? dto.getRole() : "RECEPTIONIST");
        entity.setAddress(dto.getAddress());
        entity.setEmergencyContact(dto.getEmergencyContact());
        entity.setUserName(dto.getUserName() != null ? dto.getUserName() : dto.getContactNumber());
        entity.setPassword(dto.getPassword());
        entity.setFullName(dto.getFullName());
        entity.setDateOfBirth(dto.getDateOfBirth());
        entity.setContactNumber(dto.getContactNumber());
        entity.setQualification(dto.getQualification());
        entity.setGovernmentId(dto.getGovernmentId());
        entity.setDateOfJoining(dto.getDateOfJoining());
        entity.setDepartment(dto.getDepartment());
        entity.setBankAccountDetails(dto.getBankAccountDetails());
        entity.setEmailId(dto.getEmailId());
        entity.setGraduationCertificate(dto.getGraduationCertificate());
        entity.setComputerSkillsProof(dto.getComputerSkillsProof());
        entity.setPreviousEmploymentHistory(dto.getPreviousEmploymentHistory());
    }
}
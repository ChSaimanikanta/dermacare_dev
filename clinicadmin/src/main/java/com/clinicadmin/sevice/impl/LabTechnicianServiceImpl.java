package com.clinicadmin.sevice.impl;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.clinicadmin.dto.LabTechnicanRestPassword;
import com.clinicadmin.dto.LabTechnicianLogin;
import com.clinicadmin.dto.LabTechnicianRequestDTO;
import com.clinicadmin.dto.ResponseStructure;
import com.clinicadmin.entity.LabTechnicianEntity;
import com.clinicadmin.repository.LabTechnicianRepository;
import com.clinicadmin.service.LabTechnicianService;
import com.clinicadmin.utils.LabTechnicianMapper;

@Service
public class LabTechnicianServiceImpl implements LabTechnicianService {

    @Autowired
    private LabTechnicianRepository repository;

    // ✅ Create Lab Technician
    @Override
    public ResponseStructure<LabTechnicianRequestDTO> createLabTechnician(LabTechnicianRequestDTO dto) {
        if (repository.existsByContactNumber(dto.getContactNumber())) {
            return ResponseStructure.buildResponse(
                    null,
                    "Lab Technician with this contact number already exists",
                    HttpStatus.CONFLICT,
                    HttpStatus.CONFLICT.value()
            );
        }

        LabTechnicianEntity entity = LabTechnicianMapper.toEntity(dto);
        entity.setId(generateLabTechId());
        entity.setUserName(dto.getContactNumber()); // username = contact number
        entity.setPassword(generateStructuredPassword()); // random password

        LabTechnicianEntity saved = repository.save(entity);

        LabTechnicianRequestDTO responseDTO = LabTechnicianMapper.toResponseDTO(saved);
        responseDTO.setUserName(saved.getUserName());
        responseDTO.setPassword(saved.getPassword()); // expose only on create

        return ResponseStructure.buildResponse(
                responseDTO,
                "Lab Technician created successfully",
                HttpStatus.CREATED,
                HttpStatus.CREATED.value()
        );
    }

    // ✅ Login Method
    @Override
    public ResponseStructure<String> login(LabTechnicianLogin loginRequest) {
        Optional<LabTechnicianEntity> optional = 
                repository.findByUserNameAndPassword(loginRequest.getUserName(), loginRequest.getPassword());

        if (optional.isEmpty()) {
            return ResponseStructure.buildResponse(
                    null,
                    "Invalid username or password",
                    HttpStatus.UNAUTHORIZED,
                    HttpStatus.UNAUTHORIZED.value()
            );
        }

        LabTechnicianEntity user = optional.get();
        return ResponseStructure.buildResponse(
                "Login successful. Role: " + user.getRole(),
                "Login successful",
                HttpStatus.OK,
                HttpStatus.OK.value()
        );
    }

    
    @Override
    public ResponseStructure<String> resetPassword(String contactNumber, LabTechnicanRestPassword request) {
        LabTechnicianEntity entity = repository.findByContactNumber(contactNumber)
                .orElseThrow(() -> new RuntimeException("User not found with contactNumber: " + contactNumber));

        ResponseStructure<String> response = new ResponseStructure<>();

        // check current password
        if (!entity.getPassword().equals(request.getCurrentpassword())) {
            response.setData(null);
            response.setMessage("Current password is incorrect");
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            return response;
        }

        // check new & confirm match
        if (!request.getNewPassword().equals(request.getConformPassword())) {
            response.setData(null);
            response.setMessage("New password and Confirm password do not match");
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            return response;
        }

        // update password
        entity.setPassword(request.getNewPassword());
        repository.save(entity);

        response.setData("Password updated successfully");
        response.setMessage("Success");
        response.setHttpStatus(HttpStatus.OK);
        return response;
    }

    // ✅ Get by ID
    @Override
    public ResponseStructure<LabTechnicianRequestDTO> getLabTechnicianById(String id) {
        Optional<LabTechnicianEntity> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return ResponseStructure.buildResponse(
                    null,
                    "Lab Technician not found",
                    HttpStatus.NOT_FOUND,
                    HttpStatus.NOT_FOUND.value()
            );
        }
        return ResponseStructure.buildResponse(
                LabTechnicianMapper.toResponseDTO(optional.get()),
                "Lab Technician retrieved successfully",
                HttpStatus.OK,
                HttpStatus.OK.value()
        );
    }

    // ✅ Get All
    @Override
    public ResponseStructure<List<LabTechnicianRequestDTO>> getAllLabTechnicians() {
        List<LabTechnicianEntity> entities = repository.findAll();
        List<LabTechnicianRequestDTO> dtos = entities.stream()
                .map(LabTechnicianMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseStructure.buildResponse(
                dtos,
                dtos.isEmpty() ? "No Lab Technicians found" : "Lab Technicians retrieved successfully",
                HttpStatus.OK,
                HttpStatus.OK.value()
        );
    }

    // ✅ Update
    @Override
    public ResponseStructure<LabTechnicianRequestDTO> updateLabTechnician(String id, LabTechnicianRequestDTO dto) {
        Optional<LabTechnicianEntity> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return ResponseStructure.buildResponse(
                    null,
                    "Lab Technician not found",
                    HttpStatus.NOT_FOUND,
                    HttpStatus.NOT_FOUND.value()
            );
        }

        LabTechnicianEntity existing = optional.get();

        // update fields only if provided
        if (dto.getFullName() != null) existing.setFullName(dto.getFullName());
        if (dto.getGender() != null) existing.setGender(dto.getGender());
        if (dto.getDateOfBirth() != null) existing.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getContactNumber() != null) existing.setContactNumber(dto.getContactNumber());
        if (dto.getGovernmentId() != null) existing.setGovernmentId(dto.getGovernmentId());
        if (dto.getQualificationOrCertifications() != null) existing.setQualificationOrCertifications(dto.getQualificationOrCertifications());
        if (dto.getDateOfJoining() != null) existing.setDateOfJoining(dto.getDateOfJoining());
        if (dto.getDepartmentOrAssignedLab() != null) existing.setDepartmentOrAssignedLab(dto.getDepartmentOrAssignedLab());
        if (dto.getYearOfExperience() != null) existing.setYearOfExperience(dto.getYearOfExperience());
        if (dto.getSpecialization() != null) existing.setSpecialization(dto.getSpecialization());
        if (dto.getShiftTimingsOrAvailability() != null) existing.setShiftTimingsOrAvailability(dto.getShiftTimingsOrAvailability());
        if (dto.getAddress() != null) existing.setAddress(dto.getAddress());
        if (dto.getEmergencyContact() != null) existing.setEmergencyContact(dto.getEmergencyContact());
        if (dto.getBankAccountDetails() != null) existing.setBankAccountDetails(dto.getBankAccountDetails());
        if (dto.getMedicalFitnessCertificate() != null) existing.setMedicalFitnessCertificate(dto.getMedicalFitnessCertificate());
        if (dto.getEmailId() != null) existing.setEmailId(dto.getEmailId());
        if (dto.getLabLicenseOrRegistration() != null) existing.setLabLicenseOrRegistration(dto.getLabLicenseOrRegistration());
        if (dto.getVaccinationStatus() != null) existing.setVaccinationStatus(dto.getVaccinationStatus());
        if (dto.getPreviousEmploymentHistory() != null) existing.setPreviousEmploymentHistory(dto.getPreviousEmploymentHistory());

        LabTechnicianEntity updated = repository.save(existing);

        return ResponseStructure.buildResponse(
                LabTechnicianMapper.toResponseDTO(updated),
                "Lab Technician updated successfully",
                HttpStatus.OK,
                HttpStatus.OK.value()
        );
    }

    // ✅ Delete
    @Override
    public ResponseStructure<String> deleteLabTechnician(String id) {
        Optional<LabTechnicianEntity> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return ResponseStructure.buildResponse(
                    null,
                    "Lab Technician not found",
                    HttpStatus.NOT_FOUND,
                    HttpStatus.NOT_FOUND.value()
            );
        }
        repository.deleteById(id);
        return ResponseStructure.buildResponse(
                "Deleted Successfully",
                "Lab Technician deleted successfully",
                HttpStatus.OK,
                HttpStatus.OK.value()
        );
    }

    // ----------------- Helper methods -------------------

    private String generateLabTechId() {
        return "LAB-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateStructuredPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
 // ✅ Get all Lab Technicians by Clinic Id
    @Override
    public ResponseStructure<List<LabTechnicianRequestDTO>> getLabTechniciansByClinic(String clinicId) {
        List<LabTechnicianEntity> entities = repository.findByClinicId(clinicId);

        List<LabTechnicianRequestDTO> dtos = entities.stream()
                .map(LabTechnicianMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseStructure.buildResponse(
                dtos,
                dtos.isEmpty() ? "No lab technicians found for clinic " + clinicId
                               : "Lab technicians retrieved successfully",
                HttpStatus.OK,
                HttpStatus.OK.value()
        );
    }

    // ✅ Get single Lab Technician by Clinic Id and Technician Id
    @Override
    public ResponseStructure<LabTechnicianRequestDTO> getLabTechnicianByClinicAndId(String clinicId, String technicianId) {
        LabTechnicianEntity entity = repository.findByClinicIdAndId(clinicId, technicianId)
                .orElseThrow(() -> new RuntimeException(
                        "Lab Technician not found with clinicId: " + clinicId + " and technicianId: " + technicianId));

        LabTechnicianRequestDTO dto = LabTechnicianMapper.toDTO(entity);

        return ResponseStructure.<LabTechnicianRequestDTO>builder()
                .statusCode(200)
                .message("Lab Technician data fetched successfully")
                .data(dto)
                .build();
    }
}
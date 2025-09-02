package com.clinicadmin.sevice.impl;

import com.clinicadmin.dto.LabTechnicanRestPassword;
import com.clinicadmin.dto.ReceptionistRequestDTO;
import com.clinicadmin.dto.ReceptionistRestPassword;
import com.clinicadmin.dto.ResponseStructure;
import com.clinicadmin.entity.LabTechnicianEntity;
import com.clinicadmin.entity.ReceptionistEntity;
import com.clinicadmin.repository.ReceptionistRepository;
import com.clinicadmin.service.ReceptionistService;
import com.clinicadmin.utils.ReceptionistMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReceptionistServiceImpl implements ReceptionistService {

    @Autowired
    private ReceptionistRepository repository;

    @Override
    public ResponseStructure<ReceptionistRequestDTO> createReceptionist(ReceptionistRequestDTO dto) {
        if (repository.existsByContactNumber(dto.getContactNumber())) {
            return ResponseStructure.buildResponse(
                    null,
                    "Receptionist with this contact number already exists",
                    HttpStatus.CONFLICT,
                    HttpStatus.CONFLICT.value()
            );
        }

        ReceptionistEntity entity = ReceptionistMapper.toEntity(dto);
        entity.setId(generateReceptionistId());
        entity.setUserName(dto.getContactNumber());
        entity.setPassword(generateStructuredPassword());

        ReceptionistEntity saved = repository.save(entity);

        ReceptionistRequestDTO responseDTO = ReceptionistMapper.toDTO(saved);
        responseDTO.setUserName(saved.getUserName());
        responseDTO.setPassword(saved.getPassword()); // expose only on create

        return ResponseStructure.buildResponse(
                responseDTO,
                "Receptionist created successfully",
                HttpStatus.CREATED,
                HttpStatus.CREATED.value()
        );
    }

    @Override
    public ResponseStructure<ReceptionistRequestDTO> getReceptionistById(String id) {
        Optional<ReceptionistEntity> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return ResponseStructure.buildResponse(
                    null,
                    "Receptionist not found",
                    HttpStatus.NOT_FOUND,
                    HttpStatus.NOT_FOUND.value()
            );
        }
        return ResponseStructure.buildResponse(
                ReceptionistMapper.toDTO(optional.get()),
                "Receptionist retrieved successfully",
                HttpStatus.OK,
                HttpStatus.OK.value()
        );
    }

    @Override
    public ResponseStructure<List<ReceptionistRequestDTO>> getAllReceptionists() {
        List<ReceptionistEntity> entities = repository.findAll();
        List<ReceptionistRequestDTO> dtos = entities.stream()
                .map(ReceptionistMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseStructure.buildResponse(
                dtos,
                dtos.isEmpty() ? "No Receptionists found" : "Receptionists retrieved successfully",
                HttpStatus.OK,
                HttpStatus.OK.value()
        );
    }

    @Override
    public ResponseStructure<ReceptionistRequestDTO> updateReceptionist(String id, ReceptionistRequestDTO dto) {
        Optional<ReceptionistEntity> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return ResponseStructure.buildResponse(
                    null,
                    "Receptionist not found",
                    HttpStatus.NOT_FOUND,
                    HttpStatus.NOT_FOUND.value()
            );
        }

        ReceptionistEntity existing = optional.get();

        if (dto.getFullName() != null) existing.setFullName(dto.getFullName());
        if (dto.getDateOfBirth() != null) existing.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getContactNumber() != null) existing.setContactNumber(dto.getContactNumber());
        if (dto.getQualification() != null) existing.setQualification(dto.getQualification());
        if (dto.getGovernmentId() != null) existing.setGovernmentId(dto.getGovernmentId());
        if (dto.getDateOfJoining() != null) existing.setDateOfJoining(dto.getDateOfJoining());
        if (dto.getDepartment() != null) existing.setDepartment(dto.getDepartment());
        if (dto.getAddress() != null) existing.setAddress(dto.getAddress());
        if (dto.getEmergencyContact() != null) existing.setEmergencyContact(dto.getEmergencyContact());
        if (dto.getBankAccountDetails() != null) existing.setBankAccountDetails(dto.getBankAccountDetails());
        if (dto.getEmailId() != null) existing.setEmailId(dto.getEmailId());
        if (dto.getGraduationCertificate() != null) existing.setGraduationCertificate(dto.getGraduationCertificate());
        if (dto.getComputerSkillsProof() != null) existing.setComputerSkillsProof(dto.getComputerSkillsProof());
        if (dto.getPreviousEmploymentHistory() != null) existing.setPreviousEmploymentHistory(dto.getPreviousEmploymentHistory());

        ReceptionistEntity updated = repository.save(existing);

        return ResponseStructure.buildResponse(
                ReceptionistMapper.toDTO(updated),
                "Receptionist updated successfully",
                HttpStatus.OK,
                HttpStatus.OK.value()
        );
    }

    @Override
    public ResponseStructure<String> deleteReceptionist(String id) {
        Optional<ReceptionistEntity> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return ResponseStructure.buildResponse(
                    null,
                    "Receptionist not found",
                    HttpStatus.NOT_FOUND,
                    HttpStatus.NOT_FOUND.value()
            );
        }
        repository.deleteById(id);
        return ResponseStructure.buildResponse(
                "Deleted Successfully",
                "Receptionist deleted successfully",
                HttpStatus.OK,
                HttpStatus.OK.value()
        );
    }

    @Override
    public ResponseStructure<String> login(String userName, String password) {
        Optional<ReceptionistEntity> optional = repository.findByUserName(userName);

        if (optional.isEmpty()) {
            return ResponseStructure.buildResponse(
                    null,
                    "Invalid username or password",
                    HttpStatus.UNAUTHORIZED,
                    HttpStatus.UNAUTHORIZED.value()
            );
        }

        ReceptionistEntity user = optional.get();

        // Check password
        if (!user.getPassword().equals(password)) {
            return ResponseStructure.buildResponse(
                    null,
                    "Invalid username or password",
                    HttpStatus.UNAUTHORIZED,
                    HttpStatus.UNAUTHORIZED.value()
            );
        }

        return ResponseStructure.buildResponse(
                "Login successful. Role: " + user.getRole(),
                "Login successful",
                HttpStatus.OK,
                HttpStatus.OK.value()
        );
    }

    @Override
    public ResponseStructure<String> resetPassword(String contactNumber, ReceptionistRestPassword request) {
        ReceptionistEntity entity = repository.findByContactNumber(contactNumber)
                .orElseThrow(() -> new RuntimeException("Receptionist not found with contact number: " + contactNumber));

        ResponseStructure<String> response = new ResponseStructure<>();

        // Check current password
        if (!entity.getPassword().equals(request.getCurrentpassword())) {
            response.setData(null);
            response.setMessage("Current password is incorrect");
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            return response;
        }

        // Check new password & confirm password match
        if (!request.getNewPassword().equals(request.getConformPassword())) {
            response.setData(null);
            response.setMessage("New password and Confirm password do not match");
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            return response;
        }

        // Update password
        entity.setPassword(request.getNewPassword());
        repository.save(entity);

        response.setData("Password updated successfully");
        response.setMessage("Success");
        response.setHttpStatus(HttpStatus.OK);
        return response;
    }

    // ----------------- Helper methods -------------------
    private String generateReceptionistId() {
        return "REC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
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
    
    //get all staff related to receptionist by clinic id//
    @Override
    public ResponseStructure<List<ReceptionistRequestDTO>> getReceptionistsByClinic(String clinicId) {
        List<ReceptionistEntity> entities = repository.findByClinicId(clinicId);  // ✅ should now work
        List<ReceptionistRequestDTO> dtos = entities.stream()
                .map(ReceptionistMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseStructure.buildResponse(
                dtos,
                dtos.isEmpty() ? "No receptionists found for clinic " + clinicId
                               : "Receptionists retrieved successfully",
                HttpStatus.OK,
                HttpStatus.OK.value()
        );
    }
    
    
    //get Receptionist by clinic Id and recepsitionist Id//
    @Override
    public ResponseStructure<ReceptionistRequestDTO> getReceptionistByClinicAndId(String clinicId, String receptionistId) {
        ReceptionistEntity entity = repository.findByClinicIdAndId(clinicId, receptionistId)
                .orElseThrow(() -> new RuntimeException(
                        "Receptionist not found with clinicId: " + clinicId + " and receptionistId: " + receptionistId));

        // ✅ fix: call static mapper method correctly
        ReceptionistRequestDTO dto = ReceptionistMapper.toDTO(entity);

        return ResponseStructure.<ReceptionistRequestDTO>builder()
                .statusCode(200)
                .message("Receptionist data fetched successfully")
                .data(dto)
                .build();
    }

}
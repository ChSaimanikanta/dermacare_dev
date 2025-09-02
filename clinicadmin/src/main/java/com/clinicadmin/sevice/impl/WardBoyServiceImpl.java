package com.clinicadmin.sevice.impl;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.clinicadmin.dto.ResponseStructure;
import com.clinicadmin.dto.WardBoyDTO;
import com.clinicadmin.entity.WardBoy;
import com.clinicadmin.repository.WardBoyRepository;
import com.clinicadmin.service.WardBoyService;
import com.clinicadmin.utils.WardBoyMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WardBoyServiceImpl implements WardBoyService {
	
   @Autowired
    private  WardBoyRepository wardBoyRepository;
    
    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    private String generateWardBoyId() {
        return "WB_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }

    // Fully random password generator with prefix "WB@"
    private String generatePassword(int length) {
        StringBuilder sb = new StringBuilder("WB@");
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHAR_POOL.length());
            sb.append(CHAR_POOL.charAt(index));
        }
        return sb.toString();
    }

    @Override
    public ResponseStructure<WardBoyDTO> addWardBoy(WardBoyDTO dto) {
        if (wardBoyRepository.findByContactNumber(dto.getContactNumber()).isPresent() ||
            wardBoyRepository.findByUsername(dto.getContactNumber()).isPresent()) {

            return ResponseStructure.buildResponse(
                    null,
                    "WardBoy already exists with contact number or username: " + dto.getContactNumber(),
                    HttpStatus.CONFLICT,
                    HttpStatus.CONFLICT.value()
            );
        }

        WardBoy wardBoy = WardBoyMapper.toEntity(dto);
        wardBoy.setWardBoyId(generateWardBoyId());
        wardBoy.setUsername(dto.getContactNumber());
        wardBoy.setPassword(generatePassword(6)); // WB@ + 6 random chars

        WardBoy saved = wardBoyRepository.save(wardBoy);
        WardBoyDTO responseDto = WardBoyMapper.toDTO(saved);

        return ResponseStructure.buildResponse(
                responseDto,
                "WardBoy added successfully",
                HttpStatus.CREATED,
                HttpStatus.CREATED.value()
        );
    }


    @Override
    public ResponseStructure<WardBoyDTO> getWardBoyById(String id) {
        WardBoy wardBoy = wardBoyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WardBoy not found with ID: " + id));
        WardBoyDTO dto = WardBoyMapper.toDTO(wardBoy);
        return ResponseStructure.buildResponse(dto, "WardBoy fetched successfully", HttpStatus.OK, HttpStatus.OK.value());
    }

    @Override
    public ResponseStructure<List<WardBoyDTO>> getAllWardBoys() {
        List<WardBoyDTO> wardBoys = wardBoyRepository.findAll()
                .stream()
                .map(WardBoyMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseStructure.buildResponse(wardBoys, "All WardBoys fetched successfully", HttpStatus.OK, HttpStatus.OK.value());
    }

    @Override
    public ResponseStructure<WardBoyDTO> updateWardBoy(String id, WardBoyDTO dto) {
        WardBoy existing = wardBoyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WardBoy not found with ID: " + id));

      
        if (dto.getContactNumber() != null && !existing.getContactNumber().equals(dto.getContactNumber())) {
            if (wardBoyRepository.findByContactNumber(dto.getContactNumber()).isPresent()) {
                throw new RuntimeException("WardBoy already exists with contact number: " + dto.getContactNumber());
            }
            existing.setContactNumber(dto.getContactNumber());
        }

       
        if (dto.getFullName() != null) existing.setFullName(dto.getFullName());
        if (dto.getClinicId() != null) existing.setClinicId(dto.getClinicId());
        if (dto.getDateOfBirth() != null) existing.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getGovernmentId() != null) existing.setGovernmentId(dto.getGovernmentId());
        if (dto.getDateOfJoining() != null) existing.setDateOfJoining(dto.getDateOfJoining());
        if (dto.getDepartment() != null) existing.setDepartment(dto.getDepartment());
        if (dto.getBankAccountDetails() != null) existing.setBankAccountDetails(dto.getBankAccountDetails());
        if (dto.getAddress() != null) existing.setAddress(dto.getAddress());
        if (dto.getGender() != null) existing.setGender(dto.getGender());
        if (dto.getWorkExprience() != null) existing.setWorkExprience(dto.getWorkExprience());
        if (dto.getShiftTimingOrAvailability() != null) existing.setShiftTimingOrAvailability(dto.getShiftTimingOrAvailability());
        if (dto.getEmergencyContact() != null) existing.setEmergencyContact(dto.getEmergencyContact());

        if (dto.getMedicalFitnessCertificate() != null) 
            existing.setMedicalFitnessCertificate(WardBoyMapper.toEntity(dto).getMedicalFitnessCertificate());
        if (dto.getBasicHealthFirstAidTrainingCertificate() != null) 
            existing.setBasicHealthFirstAidTrainingCertificate(WardBoyMapper.toEntity(dto).getBasicHealthFirstAidTrainingCertificate());
        if (dto.getPoliceVerification() != null) 
            existing.setPoliceVerification(WardBoyMapper.toEntity(dto).getPoliceVerification());

        if (dto.getEmailId() != null) existing.setEmailId(dto.getEmailId());
        if (dto.getPreviousEmploymentHistory() != null) existing.setPreviousEmploymentHistory(dto.getPreviousEmploymentHistory());
        if (dto.getRole() != null) existing.setRole(dto.getRole());

      
        
        
        
        
        
        existing.setUsername(existing.getUsername());
        existing.setPassword(existing.getPassword());

        WardBoy saved = wardBoyRepository.save(existing);
        WardBoyDTO responseDto = WardBoyMapper.toDTO(saved);

        return ResponseStructure.buildResponse(
                responseDto,
                "WardBoy updated successfully",
                HttpStatus.OK,
                HttpStatus.OK.value()
        );
    }

    @Override
    public ResponseStructure<Void> deleteWardBoy(String id) {
        if (!wardBoyRepository.existsById(id)) {
            throw new RuntimeException("WardBoy not found with ID: " + id);
        }
        wardBoyRepository.deleteById(id);

        return ResponseStructure.buildResponse(null, "WardBoy deleted successfully", HttpStatus.OK, HttpStatus.OK.value());
    }
    @Override
    public ResponseStructure<List<WardBoyDTO>> getWardBoysByClinicId(String clinicId) {
        List<WardBoyDTO> wardBoys = wardBoyRepository.findAllByClinicId(clinicId)
                .stream()
                .map(WardBoyMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseStructure.buildResponse(
                wardBoys,
                "WardBoys fetched successfully for clinicId: " + clinicId,
                HttpStatus.OK,
                HttpStatus.OK.value()
        );
    }


    @Override
    public ResponseStructure<WardBoyDTO> getWardBoyByIdAndClinicId(String wardBoyId, String clinicId) {
        WardBoy wardBoy = wardBoyRepository.findByWardBoyIdAndClinicId(wardBoyId, clinicId)
                .orElseThrow(() -> new RuntimeException("WardBoy not found with ID: " + wardBoyId + " for clinicId: " + clinicId));

        WardBoyDTO dto = WardBoyMapper.toDTO(wardBoy);
        return ResponseStructure.buildResponse(dto, "WardBoy fetched successfully", HttpStatus.OK, HttpStatus.OK.value());
    }

   
}
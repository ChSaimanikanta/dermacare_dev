package com.clinicadmin.sevice.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.clinicadmin.dto.ResponseStructure;
import com.clinicadmin.dto.SecurityStaffDTO;
import com.clinicadmin.entity.SecurityStaff;
import com.clinicadmin.repository.SecurityStaffRepository;
import com.clinicadmin.service.SecurityStaffService;
import com.clinicadmin.utils.IdGenerator;
import com.clinicadmin.utils.SecurityStaffMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityStaffServiceImpl implements SecurityStaffService {
   
	@Autowired
    private  SecurityStaffRepository repository;


    @Override
    public ResponseStructure<SecurityStaffDTO> addSecurityStaff(SecurityStaffDTO dto) {

        // Check if contact number already exists
        List<SecurityStaff> existingContacts = repository.findByContactNumber(dto.getContactNumber());
        if (!existingContacts.isEmpty()) {
            return ResponseStructure.buildResponse(
                    null,
                    "Contact number already exists",
                    HttpStatus.CONFLICT,
                    HttpStatus.CONFLICT.value()
            );}
dto.setSecurityStaffId(IdGenerator.generateSecurityStaffId());
        SecurityStaff staff = SecurityStaffMapper.toEntity(dto);
        SecurityStaff saved = repository.save(staff);

        return ResponseStructure.buildResponse(
                SecurityStaffMapper.toDTO(saved),
                "Security staff added successfully",
                HttpStatus.CREATED,
                HttpStatus.CREATED.value()
        );
    }

    @Override
    public ResponseStructure<SecurityStaff> updateSecurityStaff(SecurityStaff staff) {
        Optional<SecurityStaff> existingOpt = repository.findById(staff.getSecurityStaffId());
        if (existingOpt.isEmpty()) {
            return ResponseStructure.buildResponse(
                    null,
                    "Security staff not found",
                    HttpStatus.NOT_FOUND,
                    HttpStatus.NOT_FOUND.value()
            );
        }

        List<SecurityStaff> contactOwners = repository.findByContactNumber(staff.getContactNumber());
        boolean conflict = contactOwners.stream()
                .anyMatch(s -> !s.getSecurityStaffId().equals(staff.getSecurityStaffId()));

        if (conflict) {
            return ResponseStructure.buildResponse(
                    null,
                    "Contact number already exists",
                    HttpStatus.CONFLICT,
                    HttpStatus.CONFLICT.value()
            );
        }

        SecurityStaff existing = existingOpt.get();

        existing.setFullName(staff.getFullName());
        existing.setDateOfBirth(staff.getDateOfBirth());
        existing.setGender(staff.getGender());
        existing.setContactNumber(staff.getContactNumber());
        existing.setGovermentId(staff.getGovermentId());
        existing.setDateOfJoining(staff.getDateOfJoining());
        existing.setDepartment(staff.getDepartment());
        existing.setAddress(staff.getAddress());
        existing.setBankAccountDetails(staff.getBankAccountDetails());
        existing.setPoliceVerification(staff.getPoliceVerification());
        existing.setMedicalFitnessCertificate(staff.getMedicalFitnessCertificate());
        existing.setEmailId(staff.getEmailId());
        existing.setTraningOrGuardLicense(staff.getTraningOrGuardLicense());
        existing.setPreviousEmployeeHistory(staff.getPreviousEmployeeHistory());

        SecurityStaff updated = repository.save(existing);

        return ResponseStructure.buildResponse(
                updated,
                "Security staff updated successfully",
                HttpStatus.OK,
                HttpStatus.OK.value()
        );
    }

    @Override
    public ResponseStructure<SecurityStaffDTO> getSecurityStaffById(String staffId) {
        return repository.findById(staffId)
                .map(staff -> ResponseStructure.buildResponse(
                        SecurityStaffMapper.toDTO(staff),
                        "Staff found",
                        HttpStatus.OK,
                        HttpStatus.OK.value()
                ))
                .orElse(ResponseStructure.buildResponse(
                        null,
                        "Staff not found",
                        HttpStatus.NOT_FOUND,
                        HttpStatus.NOT_FOUND.value()
                ));
    }

    @Override
    public ResponseStructure<List<SecurityStaffDTO>> getAllByClinicId(String clinicId) {
        List<SecurityStaff> staffList = repository.findByClinicId(clinicId);

        List<SecurityStaffDTO> dtoList = staffList.stream()
                .map(SecurityStaffMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseStructure.buildResponse(
                dtoList,
                dtoList.isEmpty() ? "No staff found for this clinic" : "Staff list fetched",
                HttpStatus.OK,
                HttpStatus.OK.value()
        );
    }

    @Override
    public ResponseStructure<String> deleteSecurityStaff(String staffId) {
        Optional<SecurityStaff> existing = repository.findById(staffId);
        if (existing.isEmpty()) {
            return ResponseStructure.buildResponse(
                    null,
                    "Staff not found",
                    HttpStatus.NOT_FOUND,
                    HttpStatus.NOT_FOUND.value()
            );
        }

        repository.deleteById(staffId);
        return ResponseStructure.buildResponse(
                staffId,
                "Staff deleted successfully",
                HttpStatus.OK,
                HttpStatus.OK.value()
        );
    }
}
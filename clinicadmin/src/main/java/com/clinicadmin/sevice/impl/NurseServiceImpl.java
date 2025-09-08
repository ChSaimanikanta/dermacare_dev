package com.clinicadmin.sevice.impl;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.clinicadmin.dto.NurseDTO;
import com.clinicadmin.dto.NurseLoginDTO;
import com.clinicadmin.dto.OnBoardResponse;
import com.clinicadmin.dto.ResetNurseLoginPasswordDTO;
import com.clinicadmin.dto.Response;
import com.clinicadmin.entity.Nurse;
import com.clinicadmin.repository.NurseRepository;
import com.clinicadmin.service.NurseService;
import com.clinicadmin.utils.Base64CompressionUtil;

@Service
public class NurseServiceImpl implements NurseService {
    @Autowired
    NurseRepository nurseRepository;

    @Override
    public Response nureseOnboarding(NurseDTO dto) {
        Response response = new Response();
        dto.trimNurseFields();

        if (nurseRepository.existsByNurseContactNumber(dto.getNurseContactNumber())) {
            response.setSuccess(false);
            response.setData(null);
            response.setMessage("Nurse with this mobile number already exists");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return response;
        }

        Nurse nurse = mapNurseDtoTONurseEntity(dto);
        String nurseId = generateNurseId();
        nurse.setNurseId(nurseId);

        String userName = dto.getNurseContactNumber();
        String password = generateStructuredPassword();

        nurse.setUserName(userName);
        nurse.setPassword(password);

        // ✅ Set default role if not provided
        if (nurse.getRole() == null || nurse.getRole().isBlank()) {
            nurse.setRole("NURSE");
        }

        Nurse savedNurse = nurseRepository.save(nurse);
        NurseDTO savedNurseDTO = mapNurseEntityToNurseDTO(savedNurse);

        savedNurseDTO.setUserName(savedNurse.getUserName());
        savedNurseDTO.setPassword(savedNurse.getPassword());

        response.setSuccess(true);
        response.setData(savedNurseDTO);
        response.setMessage("Nurse added successfully ");
        response.setStatus(HttpStatus.CREATED.value());

        return response;
    }

    @Override
    public Response getAllNursesByHospital(String hospitalId) {
        Response response = new Response();

        if (hospitalId == null || hospitalId.isBlank()) {
            response.setSuccess(false);
            response.setData(null);
            response.setMessage("Hospital ID must not be empty");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return response;
        }

        List<Nurse> nurses = nurseRepository.findByHospitalId(hospitalId);
        if (nurses.isEmpty()) {
            response.setSuccess(true);
            response.setData(List.of());
            response.setMessage("No nurses found for this hospital");
            response.setStatus(HttpStatus.OK.value());
            return response;
        }

        List<NurseDTO> nurseDTOs = nurses.stream().map(this::mapNurseEntityToNurseDTO).toList();
        response.setSuccess(true);
        response.setData(nurseDTOs);
        response.setMessage("Nurses fetched successfully");
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    @Override
    public Response getNurseByHospitalAndNurseId(String hospitalId, String nurseId) {
        Response response = new Response();

        if (hospitalId == null || hospitalId.isBlank() || nurseId == null || nurseId.isBlank()) {
            response.setSuccess(false);
            response.setData(null);
            response.setMessage("Hospital ID and Nurse ID must not be empty");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return response;
        }

        return nurseRepository.findByHospitalIdAndNurseId(hospitalId, nurseId).map(nurse -> {
            NurseDTO dto = mapNurseEntityToNurseDTO(nurse);
            response.setSuccess(true);
            response.setData(dto);
            response.setMessage("Nurse found");
            response.setStatus(HttpStatus.OK.value());
            return response;
        }).orElseGet(() -> {
            response.setSuccess(false);
            response.setData(null);
            response.setMessage("Nurse not found");
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return response;
        });
    }

    @Override
    public Response updateNurse(String hospitalId, String nurseId, NurseDTO dto) {
        Response response = new Response();

        if (hospitalId == null || hospitalId.isBlank() || nurseId == null || nurseId.isBlank()) {
            response.setSuccess(false);
            response.setData(null);
            response.setMessage("Hospital ID and Nurse ID must not be empty");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return response;
        }

        return nurseRepository.findByHospitalIdAndNurseId(hospitalId, nurseId).map(existingNurse -> {
            if (dto.getFullName() != null) existingNurse.setFullName(dto.getFullName());
            if (dto.getDateOfBirth() != null) existingNurse.setDateOfBirth(dto.getDateOfBirth());
            if (dto.getDepartment() != null) existingNurse.setDepartment(dto.getDepartment());
            if (dto.getEmailId() != null) existingNurse.setEmailId(dto.getEmailId());
            if (dto.getNurseContactNumber() != null) existingNurse.setNurseContactNumber(dto.getNurseContactNumber());
            if (dto.getGovernmentId() != null) existingNurse.setGovernmentId(dto.getGovernmentId());
            if (dto.getBankAccountDetails() != null) existingNurse.setBankAccountDetails(dto.getBankAccountDetails());
            if (dto.getInsuranceOrESIdetails() != null) existingNurse.setInsuranceOrESIdetails(dto.getInsuranceOrESIdetails());
            existingNurse.setVaccinationStatus(dto.isVaccinationStatus());
            if (dto.getPreviousEmploymentHistory() != null) existingNurse.setPreviousEmploymentHistory(dto.getPreviousEmploymentHistory());
            if (dto.getAddress() != null) existingNurse.setAddress(dto.getAddress());
            if (dto.getGender() != null) existingNurse.setGender(dto.getGender());
            if (dto.getEmergencyContactNumber() != null) existingNurse.setEmergencyContactNumber(dto.getEmergencyContactNumber());
            if (dto.getYearsOfExperience() != null) existingNurse.setYearsOfExperience(dto.getYearsOfExperience());
            if (dto.getQualifications() != null) existingNurse.setQualifications(dto.getQualifications());
            if (dto.getShiftTimingOrAvailability() != null) existingNurse.setShiftTimingOrAvailability(dto.getShiftTimingOrAvailability());

            // ✅ Handle profile picture (Base64)
            if (dto.getProfilePicture() != null && !dto.getProfilePicture().isBlank()) {
                try {
                    byte[] decodedBytes = Base64.getDecoder().decode(dto.getProfilePicture());
                    String encodedString = Base64.getEncoder().encodeToString(decodedBytes);
                    existingNurse.setProfilePicture(encodedString);
                } catch (IllegalArgumentException e) {
                    response.setSuccess(false);
                    response.setData(null);
                    response.setMessage("Invalid Base64 format for profile picture");
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                    return response;
                }
            }

            // ✅ Ensure role is not null
            if (dto.getRole() != null && !dto.getRole().isBlank()) {
                existingNurse.setRole(dto.getRole());
            } else if (existingNurse.getRole() == null) {
                existingNurse.setRole("NURSE");
            }

            Nurse updated = nurseRepository.save(existingNurse);
            NurseDTO updatedDTO = mapNurseEntityToNurseDTO(updated);

            response.setSuccess(true);
            response.setData(updatedDTO);
            response.setMessage("Nurse updated successfully");
            response.setStatus(HttpStatus.OK.value());
            return response;
        }).orElseGet(() -> {
            response.setSuccess(false);
            response.setData(null);
            response.setMessage("Nurse not found for update");
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return response;
        });
    }

    @Override
    public Response deleteNurse(String hospitalId, String nurseId) {
        Response response = new Response();

        if (hospitalId == null || hospitalId.isBlank() || nurseId == null || nurseId.isBlank()) {
            response.setSuccess(false);
            response.setData(null);
            response.setMessage("Hospital ID and Nurse ID must not be empty");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return response;
        }

        if (nurseRepository.findByHospitalIdAndNurseId(hospitalId, nurseId).isPresent()) {
            nurseRepository.deleteByHospitalIdAndNurseId(hospitalId, nurseId);
            response.setSuccess(true);
            response.setData(null);
            response.setMessage("Nurse deleted successfully");
            response.setStatus(HttpStatus.NO_CONTENT.value());
        } else {
            response.setSuccess(false);
            response.setData(null);
            response.setMessage("Nurse not found");
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
        return response;
    }

    //---------------------------Mapper Methods--------------------------------------------------------------------------------
    private Nurse mapNurseDtoTONurseEntity(NurseDTO dto) {
        Nurse nurse = new Nurse();
        nurse.setHospitalId(dto.getHospitalId());
        nurse.setFullName(dto.getFullName());
        nurse.setDateOfBirth(dto.getDateOfBirth());
        nurse.setNurseContactNumber(dto.getNurseContactNumber());
        nurse.setGovernmentId(dto.getGovernmentId());
        nurse.setNursingLicense(Base64CompressionUtil.compressBase64(dto.getNursingLicense()));
        nurse.setNursingCouncilRegistration(Base64CompressionUtil.compressBase64(dto.getNursingCouncilRegistration()));
        nurse.setNursingDegreeOrDiplomaCertificate(Base64CompressionUtil.compressBase64(dto.getNursingDegreeOrDiplomaCertificate()));
        nurse.setDateOfJoining(dto.getDateOfJoining());
        nurse.setDepartment(dto.getDepartment());
        nurse.setBankAccountDetails(dto.getBankAccountDetails());
        nurse.setMedicalFitnessCertificate(Base64CompressionUtil.compressBase64(dto.getMedicalFitnessCertificate()));
        nurse.setEmailId(dto.getEmailId());
        nurse.setPreviousEmploymentHistory(dto.getPreviousEmploymentHistory());
        nurse.setExperienceCertificates(Base64CompressionUtil.compressBase64(dto.getExperienceCertificates()));
        nurse.setProfilePicture(Base64CompressionUtil.compressBase64(dto.getProfilePicture()));
        nurse.setVaccinationStatus(dto.isVaccinationStatus());
        nurse.setInsuranceOrESIdetails(dto.getInsuranceOrESIdetails());
        nurse.setAddress(dto.getAddress());

        // ✅ Ensure role is set
        nurse.setRole(dto.getRole() != null ? dto.getRole() : "NURSE");

        nurse.setPermissions(dto.getPermissions());
        nurse.setGender(dto.getGender());
        nurse.setQualifications(dto.getQualifications());
        nurse.setShiftTimingOrAvailability(dto.getShiftTimingOrAvailability());
        nurse.setYearsOfExperience(dto.getYearsOfExperience());
        nurse.setEmergencyContactNumber(dto.getEmergencyContactNumber());

        return nurse;
    }

    private NurseDTO mapNurseEntityToNurseDTO(Nurse nurse) {
        NurseDTO dto = new NurseDTO();
        dto.setId(nurse.getId().toString());
        dto.setNurseId(nurse.getNurseId());
        dto.setHospitalId(nurse.getHospitalId());
        dto.setRole(nurse.getRole());
        dto.setFullName(nurse.getFullName());
        dto.setDateOfBirth(nurse.getDateOfBirth());
        dto.setNurseContactNumber(nurse.getNurseContactNumber());
        dto.setGovernmentId(nurse.getGovernmentId());
        dto.setNursingLicense(Base64CompressionUtil.decompressBase64(nurse.getNursingLicense()));
        dto.setNursingDegreeOrDiplomaCertificate(Base64CompressionUtil.decompressBase64(nurse.getNursingDegreeOrDiplomaCertificate()));
        dto.setNursingCouncilRegistration(Base64CompressionUtil.decompressBase64(nurse.getNursingCouncilRegistration()));
        dto.setDateOfJoining(nurse.getDateOfJoining());
        dto.setDepartment(nurse.getDepartment());
        dto.setBankAccountDetails(nurse.getBankAccountDetails());
        dto.setMedicalFitnessCertificate(Base64CompressionUtil.decompressBase64(nurse.getMedicalFitnessCertificate()));
        dto.setEmailId(nurse.getEmailId());
        dto.setPreviousEmploymentHistory(Base64CompressionUtil.decompressBase64(nurse.getPreviousEmploymentHistory()));
        dto.setVaccinationStatus(nurse.isVaccinationStatus());
        dto.setInsuranceOrESIdetails(nurse.getInsuranceOrESIdetails());
        dto.setExperienceCertificates(Base64CompressionUtil.decompressBase64(nurse.getExperienceCertificates()));
        dto.setProfilePicture(Base64CompressionUtil.decompressBase64(nurse.getProfilePicture()));
        dto.setAddress(nurse.getAddress());
        dto.setRole(nurse.getRole());
        dto.setPermissions(nurse.getPermissions());
        dto.setGender(nurse.getGender());
        dto.setQualifications(nurse.getQualifications());
        dto.setShiftTimingOrAvailability(nurse.getShiftTimingOrAvailability());
        dto.setEmergencyContactNumber(nurse.getEmergencyContactNumber());
        dto.setYearsOfExperience(nurse.getYearsOfExperience());

        return dto;
    }

    //----------------- Helper Methods ------------------------
    private String generateNurseId() {
        return "NR_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateStructuredPassword() {
        String[] words = { "Nurse" };
        String specialChars = "@#$%&*!?";
        String digits = "0123456789";
        SecureRandom random = new SecureRandom();

        String word = words[random.nextInt(words.length)];
        String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1);
        char specialChar = specialChars.charAt(random.nextInt(specialChars.length()));

        StringBuilder numberPart = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            numberPart.append(digits.charAt(random.nextInt(digits.length())));
        }
        return capitalizedWord + specialChar + numberPart;
    }

    //--------------------------------- NurseLogin -------------------------------------------------------
    @Override
    public OnBoardResponse nurseLogin(NurseLoginDTO loginDTO) {
        Optional<Nurse> optional = nurseRepository.findByUserName(loginDTO.getUserName());

        if (optional.isEmpty()) {
            return new OnBoardResponse(
                    "Invalid username",
                    HttpStatus.NOT_FOUND,
                    HttpStatus.NOT_FOUND.value(),
                    null,
                    null,
                    null
            );
        }

        Nurse nurse = optional.get();

        if (!nurse.getPassword().equals(loginDTO.getPassword())) {
            return new OnBoardResponse(
                    "Invalid password",
                    HttpStatus.UNAUTHORIZED,
                    HttpStatus.UNAUTHORIZED.value(),
                    null,
                    null,
                    null
            );
        }

        // ✅ Wrap permissions inside role
        Map<String, Map<String, List<String>>> wrappedPermissions = Map.of(
                nurse.getRole() != null ? nurse.getRole() : "NURSE",
                nurse.getPermissions()
        );

        return new OnBoardResponse(
                "Login successful",
                HttpStatus.OK,
                HttpStatus.OK.value(),
                nurse.getRole() != null ? nurse.getRole() : "NURSE",
                nurse.getFullName(),
                wrappedPermissions
        );
    }

    //-----------------------reset nurse login password-------------------------------------------
    @Override
    public Response resetLoginPassword(ResetNurseLoginPasswordDTO dto) {
        Response response = new Response();
        Optional<Nurse> nurseFromDB = nurseRepository.findByUserName(dto.getUserName());
        if (nurseFromDB.isPresent()) {
            Nurse nurse = nurseFromDB.get();
            if (nurse.getPassword().equals(dto.getCurrentPassword())) {
                if (dto.getNewPassword().equals(dto.getConfirmPassword())) {
                    nurse.setPassword(dto.getNewPassword());
                    nurseRepository.save(nurse);
                    response.setSuccess(true);
                    response.setMessage("Password updated Successfully");
                    response.setStatus(HttpStatus.OK.value());
                } else {
                    response.setSuccess(false);
                    response.setMessage("New password and confirm password are not matched");
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                }
            } else {
                response.setSuccess(false);
                response.setMessage("Invalid password");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            }
        } else {
            response.setSuccess(false);
            response.setMessage("Invalid Username");
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
        return response;
    }
}

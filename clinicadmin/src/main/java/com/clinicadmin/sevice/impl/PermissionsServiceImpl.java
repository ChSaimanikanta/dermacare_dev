package com.clinicadmin.sevice.impl;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.clinicadmin.dto.PermissionsDTO;
import com.clinicadmin.dto.ResponseStructure;
import com.clinicadmin.entity.Permissions;
import com.clinicadmin.repository.PermissionsRepository;
import com.clinicadmin.service.PermissionsService;

@Service
public class PermissionsServiceImpl implements PermissionsService {

    @Autowired
    private PermissionsRepository permissionsRepository;

   
    // ✅ Create new permissions
    @Override
    public ResponseStructure<PermissionsDTO> createPermissions(PermissionsDTO dto) {
        Permissions saved = permissionsRepository.save(mapToEntity(dto));
        return ResponseStructure.buildResponse(
                mapToDTO(saved),
                "Permissions created successfully",
                HttpStatus.CREATED,
                HttpStatus.CREATED.value()
        );
    }


    @Override
    public ResponseStructure<PermissionsDTO> updatePermissionsById(String permissionId, PermissionsDTO dto) {
        Optional<Permissions> optionalPermission = permissionsRepository.findById(permissionId);

        if (optionalPermission.isEmpty()) {
            return ResponseStructure.buildResponse(
                    null,
                    "No permissions found with ID: " + permissionId,
                    HttpStatus.NOT_FOUND,
                    HttpStatus.NOT_FOUND.value()
            );
        }

        Permissions existingPermission = optionalPermission.get();

        // ✅ Update only the permissions field
        if (dto.getPermissions() != null && !dto.getPermissions().isEmpty()) {
            existingPermission.setPermissions(dto.getPermissions());
        }

        Permissions updatedPermission = permissionsRepository.save(existingPermission);

        return ResponseStructure.buildResponse(
                mapToDTO(updatedPermission),
                "Permissions updated successfully",
                HttpStatus.OK,
                HttpStatus.OK.value()
        );
    }

    // ✅ Delete permissions by ID
    @Override
    public ResponseStructure<String> deletePermissionsById(String permissionId) {
        if (!permissionsRepository.existsById(permissionId)) {
            return ResponseStructure.buildResponse(
                    null,
                    "No permissions found with ID: " + permissionId,
                    HttpStatus.NOT_FOUND,
                    HttpStatus.NOT_FOUND.value()
            );
        }

        permissionsRepository.deleteById(permissionId);
        return ResponseStructure.buildResponse(
                permissionId,
                "Permissions deleted successfully",
                HttpStatus.OK,
                HttpStatus.OK.value()
        );
    }

    // ✅ Get all permissions by Clinic ID
    @Override
    public ResponseStructure<List<PermissionsDTO>> getPermissionsByClinicId(String clinicId) {
        List<Permissions> list = permissionsRepository.findByClinicId(clinicId);
        List<PermissionsDTO> dtoList = list.stream().map(this::mapToDTO).collect(Collectors.toList());

        String message = dtoList.isEmpty()
                ? "No permissions found for clinic ID: " + clinicId
                : "Permissions fetched successfully for clinic ID: " + clinicId;

        return ResponseStructure.buildResponse(dtoList, message, HttpStatus.OK, HttpStatus.OK.value());
    }

    // ✅ Get all permissions by Clinic ID and Branch ID
    @Override
    public ResponseStructure<List<PermissionsDTO>> getPermissionsByClinicAndBranch(String clinicId, String branchId) {
        List<Permissions> list = permissionsRepository.findByClinicIdAndBranchId(clinicId, branchId);
        List<PermissionsDTO> dtoList = list.stream().map(this::mapToDTO).collect(Collectors.toList());

        String message = dtoList.isEmpty()
                ? "No permissions found for clinic ID: " + clinicId + " and branch ID: " + branchId
                : "Permissions fetched successfully for clinic ID: " + clinicId + " and branch ID: " + branchId;

        return ResponseStructure.buildResponse(dtoList, message, HttpStatus.OK, HttpStatus.OK.value());
    }

    // ✅ Get permissions by Clinic ID, Branch ID, and User ID
    @Override
    public ResponseStructure<PermissionsDTO> getPermissionsByClinicBranchAndUser(
            String clinicId, String branchId, String userId) {

        Optional<Permissions> optional = permissionsRepository.findByClinicIdAndBranchIdAndUserId(clinicId, branchId, userId);

        if (optional.isEmpty()) {
            return ResponseStructure.buildResponse(
                    null,
                    "No permissions found for user ID: " + userId +
                    " in clinic ID: " + clinicId + ", branch ID: " + branchId,
                    HttpStatus.NOT_FOUND,
                    HttpStatus.NOT_FOUND.value()
            );
        }

        return ResponseStructure.buildResponse(
                mapToDTO(optional.get()),
                "Permissions fetched successfully",
                HttpStatus.OK,
                HttpStatus.OK.value()
        );
    }
    // ✅ Helper: Convert DTO → Entity
    private Permissions mapToEntity(PermissionsDTO dto) {
        Permissions entity = new Permissions();
        entity.setId(dto.getId());
        entity.setClinicId(dto.getClinicId());
        entity.setBranchId(dto.getBranchId());
        entity.setUserId(dto.getUserId());
        entity.setPermissions(dto.getPermissions());
        return entity;
    }

    // ✅ Helper: Convert Entity → DTO
    private PermissionsDTO mapToDTO(Permissions entity) {
        PermissionsDTO dto = new PermissionsDTO();
        dto.setId(entity.getId());
        dto.setClinicId(entity.getClinicId());
        dto.setBranchId(entity.getBranchId());
        dto.setUserId(entity.getUserId());
        dto.setPermissions(entity.getPermissions());
        return dto;
    }

}

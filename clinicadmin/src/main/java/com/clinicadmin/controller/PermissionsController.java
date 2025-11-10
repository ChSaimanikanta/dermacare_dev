package com.clinicadmin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.clinicadmin.dto.PermissionsDTO;
import com.clinicadmin.dto.ResponseStructure;
import com.clinicadmin.service.PermissionsService;

@RestController
@RequestMapping("/clinic-admin")
public class PermissionsController {

    @Autowired
    private PermissionsService permissionsService;

    // ✅ Create Permissions
    @PostMapping("/createPermissions")
    public ResponseEntity<ResponseStructure<PermissionsDTO>> createPermissions(@RequestBody PermissionsDTO dto) {
        ResponseStructure<PermissionsDTO> response = permissionsService.createPermissions(dto);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    // ✅ Update Permissions by ID
    @PutMapping("/updatePermissionsById/{id}")
    public ResponseEntity<ResponseStructure<PermissionsDTO>> updatePermissions(
            @PathVariable String id,
            @RequestBody PermissionsDTO dto) {

        ResponseStructure<PermissionsDTO> response = permissionsService.updatePermissionsById(id, dto);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    // ✅ Delete Permissions by ID
    @DeleteMapping("/deletePermissionsById/{id}")
    public ResponseEntity<ResponseStructure<String>> deletePermissions(@PathVariable String id) {
        ResponseStructure<String> response = permissionsService.deletePermissionsById(id);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    // ✅ Get All Permissions by Clinic ID
    @GetMapping("/getPermissionsByClinicById/{clinicId}")
    public ResponseEntity<ResponseStructure<List<PermissionsDTO>>> getPermissionsByClinicId(@PathVariable String clinicId) {
        ResponseStructure<List<PermissionsDTO>> response = permissionsService.getPermissionsByClinicId(clinicId);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @GetMapping("/getPermissionsByClinicIdAndBranchId/{clinicId}/{branchId}")
    public ResponseEntity<ResponseStructure<List<PermissionsDTO>>> getPermissionsByClinicIdAndBranchId(
            @PathVariable String clinicId,
            @PathVariable String branchId) {
        ResponseStructure<List<PermissionsDTO>> response = permissionsService.getPermissionsByClinicAndBranch(clinicId, branchId);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }


    @GetMapping("/getPermissionsByClinicIdBranchIdAndUserId/{clinicId}/{branchId}/{userId}")
    public ResponseEntity<ResponseStructure<PermissionsDTO>> getPermissionsByClinicBranchAndUser(
            @PathVariable String clinicId,
            @PathVariable String branchId,
            @PathVariable String userId) {
        ResponseStructure<PermissionsDTO> response = permissionsService.getPermissionsByClinicBranchAndUser(clinicId, branchId, userId);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }
}

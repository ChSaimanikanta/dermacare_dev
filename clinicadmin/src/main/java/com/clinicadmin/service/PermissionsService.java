package com.clinicadmin.service;

import java.util.List;
import com.clinicadmin.dto.PermissionsDTO;
import com.clinicadmin.dto.ResponseStructure;

public interface PermissionsService {

    ResponseStructure<PermissionsDTO> createPermissions(PermissionsDTO dto);

    ResponseStructure<PermissionsDTO> updatePermissionsById(String permissionId, PermissionsDTO dto);

    ResponseStructure<String> deletePermissionsById(String permissionId);

    ResponseStructure<List<PermissionsDTO>> getPermissionsByClinicId(String clinicId);

    ResponseStructure<List<PermissionsDTO>> getPermissionsByClinicAndBranch(String clinicId, String branchId);

    ResponseStructure<PermissionsDTO> getPermissionsByClinicBranchAndUser(String clinicId, String branchId, String userId);
}

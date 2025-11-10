package com.clinicadmin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.clinicadmin.entity.Permissions;

@Repository
public interface PermissionsRepository extends MongoRepository<Permissions, String> {

    // 1️⃣ Find all by clinicId
    List<Permissions> findByClinicId(String clinicId);

    // 2️⃣ Find all by clinicId and branchId
    List<Permissions> findByClinicIdAndBranchId(String clinicId, String branchId);

    // 3️⃣ Find by clinicId, branchId, and userId
    Optional<Permissions> findByClinicIdAndBranchIdAndUserId(String clinicId, String branchId, String userId);
}

package com.clinicadmin.repository;

import com.clinicadmin.entity.DoctorLoginCredentials;
import com.clinicadmin.entity.Doctors;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DoctorLoginCredentialsRepository extends MongoRepository<DoctorLoginCredentials, String> {
    Optional<DoctorLoginCredentials> findByUserName(String username);
    boolean existsByUserName(String username);
	Optional<DoctorLoginCredentials> findByStaffId(String staffId);

}

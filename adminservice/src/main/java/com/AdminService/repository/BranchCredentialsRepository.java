package com.AdminService.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.AdminService.entity.BranchCredentials;

public interface BranchCredentialsRepository extends MongoRepository<BranchCredentials, String> {
    BranchCredentials findByUserName(String userName);

	BranchCredentials findByUserNameAndPassword(String userName, String password);

}

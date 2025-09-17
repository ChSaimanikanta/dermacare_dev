package com.clinicadmin.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.clinicadmin.entity.CustomerOnbording;

public interface CustomerOnboardingRepository extends MongoRepository<CustomerOnbording, String> {

	Optional<CustomerOnbording> findByCustomerId(String userName);

	Optional<CustomerOnbording> findByCustomerById(String id);
}

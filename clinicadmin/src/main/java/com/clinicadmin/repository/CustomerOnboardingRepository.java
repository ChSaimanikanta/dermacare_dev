package com.clinicadmin.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.clinicadmin.entity.CustomerOnbording;

public interface CustomerOnboardingRepository extends MongoRepository<CustomerOnbording, String> {
}

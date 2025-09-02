package com.clinicadmin.entity;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import com.clinicadmin.dto.Address;
import com.clinicadmin.dto.BankAccountDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "Pharmacist")
@AllArgsConstructor
@NoArgsConstructor
public class Pharmacist {
	private ObjectId id;
	private String role = "PHARMACIST";
	private String pharmacistId;
	private String fullName;
	private String gender;
	private String qualification;
	private String dateOfBirth;
	private String contactNumber;
	private String governmentId;
	private String pharmacyLicense;
	private String dPharmaOrBPharmaCertificate;
	private String statePharmacyCouncilRegistration;
	private String dateOfJoining;
	private String department;
	private BankAccountDetails bankAccountDetails;
	private Address address;
	// Optional Fields
	private String emailID;
	private String previousEmploymentHistory;
	private String experienceCertificates;
	private String emergencyContactNumber;
	
	private String userName;
	private String password;
}

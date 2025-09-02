package com.clinicadmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PharmacistDTO {
	private String id;
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

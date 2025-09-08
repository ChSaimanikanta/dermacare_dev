package com.clinicadmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PharmacistDTO {
	private String id;
	
	private String hospitalId;

	private String role = "PHARMACIST";
	private String pharmacistId;
	private String fullName;
	private String gender;
	private String qualification;
	private String dateOfBirth;
	private String contactNumber;
	private String governmentId;
	private String pharmacyLicense;
	private String dateOfJoining;
	private String department;
	private BankAccountDetails bankAccountDetails;
	private Address address;
	// Optional Fields
	private String emailID;
	private String previousEmploymentHistory;
	private String emergencyContactNumber;
    private String profilePicture;
    private String statePharmacyCouncilRegistration;
    private String dPharmaOrBPharmaCertificate;
    private String experienceCertificates;

	
	private String userName;
	private String password;
}

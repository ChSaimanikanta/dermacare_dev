package com.dermaCare.customerService.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Branch {
	private String id;
	private String clinicId;
	private String branchId;
	private String branchName;
	private String address;
	private String city;
	private String contactNumber;
	private String email;
	private String latitude;
	private String longitude;
	private String virtualClinicTour;

	private String role;            
	private Map<String, Map<String, List<String>>> permissions;
}

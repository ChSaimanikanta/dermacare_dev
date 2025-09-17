package com.clinicadmin.service;

import com.clinicadmin.dto.ChangeDoctorPasswordDTO;
import com.clinicadmin.dto.CustomerLoginDTO;
import com.clinicadmin.dto.CustomerOnbordingDTO;
import com.clinicadmin.dto.Response;
import com.clinicadmin.sevice.impl.CustomerResponseDTO;

public interface CustomerOnboardingService {
	Response onboardCustomer(CustomerOnbordingDTO dto);

	Response getAllCustomers();

	Response getCustomerById(String id);

	Response updateCustomer(String id, CustomerOnbordingDTO dto);

	Response deleteCustomer(String id);

	Response login(CustomerLoginDTO dto);
//
//	Response resetPassword(ChangeDoctorPasswordDTO dto);
}

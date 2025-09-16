package com.clinicadmin.service;

import com.clinicadmin.dto.CustomerOnbordingDTO;
import com.clinicadmin.dto.Response;

import java.util.List;

public interface CustomerOnboardingService {
	Response onboardCustomer(CustomerOnbordingDTO dto);

	Response getAllCustomers();

	Response getCustomerById(String id);

	Response updateCustomer(String id, CustomerOnbordingDTO dto);

	Response deleteCustomer(String id);

	Response login(String username, String password);

	Response resetPassword(String username, String oldPassword, String newPassword);
}

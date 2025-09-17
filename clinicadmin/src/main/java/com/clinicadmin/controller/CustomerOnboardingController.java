package com.clinicadmin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinicadmin.dto.CustomerLoginDTO;
import com.clinicadmin.dto.CustomerOnbordingDTO;
import com.clinicadmin.dto.Response;
import com.clinicadmin.service.CustomerOnboardingService;

@RestController
@RequestMapping("/clinic-admin")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class CustomerOnboardingController {

	@Autowired
	private CustomerOnboardingService customerOnboardingService;

	// ✅ Create / Onboard Customer
	@PostMapping("/customers/onboard")
	public ResponseEntity<Response> onboardCustomer(@RequestBody CustomerOnbordingDTO dto) {
		Response response = customerOnboardingService.onboardCustomer(dto);
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	// ✅ Get All Customers
	@GetMapping("/customers/getAllCustomers")
	public ResponseEntity<Response> getAllCustomers() {
		Response response = customerOnboardingService.getAllCustomers();
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	// ✅ Get Customer By ID
	@GetMapping("/customers/{id}")
	public ResponseEntity<Response> getCustomerById(@PathVariable String id) {
		Response response = customerOnboardingService.getCustomerById(id);
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	// ✅ Update Customer
	@PutMapping("/customers/{id}")
	public ResponseEntity<Response> updateCustomer(@PathVariable String id, @RequestBody CustomerOnbordingDTO dto) {
		Response response = customerOnboardingService.updateCustomer(id, dto);
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	// ✅ Delete Customer
	@DeleteMapping("/customers/{id}")
	public ResponseEntity<Response> deleteCustomer(@PathVariable String id) {
		Response response = customerOnboardingService.deleteCustomer(id);
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	// ✅ Login with PathVariable
	@PostMapping("/customers/login")
	public ResponseEntity<Response> login(@RequestBody CustomerLoginDTO dto) {
		Response response = customerOnboardingService.login(dto);
		return ResponseEntity.status(response.getStatus()).body(response);
	}

//	// ✅ Reset Password with PathVariable
//	@PostMapping("/customers/reset-password/{username}/{oldPassword}/{newPassword}")
//	public ResponseEntity<Response> resetPassword(@PathVariable String username, @PathVariable String oldPassword,
//			@PathVariable String newPassword) {
//		Response response = customerOnboardingService.resetPassword(username, oldPassword, newPassword);
//		return ResponseEntity.status(response.getStatus()).body(response);
//	}
}

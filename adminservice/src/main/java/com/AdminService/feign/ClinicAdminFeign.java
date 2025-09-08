package com.AdminService.feign;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.AdminService.dto.DoctorsDTO;
import com.AdminService.dto.SubServicesDto;
import com.AdminService.util.Response;
import com.AdminService.util.ResponseStructure;
@FeignClient(name = "clinicadmin")
//@CircuitBreaker(name = "circuitBreaker", fallbackMethod = "clinicAdminServiceFallBack")
public interface ClinicAdminFeign {
	
	@GetMapping("/clinic-admin/subService/getAllSubServies")
    public ResponseEntity<ResponseStructure<List<SubServicesDto>>> getAllSubServices();
		
	 // ---------------------- Doctor APIs ----------------------
    @PostMapping("/clinic-admin/addDoctor")
    ResponseEntity<Response> addDoctor(@RequestBody DoctorsDTO dto);

    @GetMapping("/clinic-admin/doctors")
    ResponseEntity<Response> getAllDoctors();
		
    @GetMapping("/clinic-admin/doctor/{id}")
    ResponseEntity<Response> getDoctorById(@PathVariable("id") String id);

    @PutMapping("/clinic-admin/updateDoctor/{doctorId}")
    ResponseEntity<Response> updateDoctorById(@PathVariable String doctorId,
                                              @Validated @RequestBody DoctorsDTO dto);
		
    @DeleteMapping("/clinic-admin/delete-doctor/{doctorId}")
    ResponseEntity<Response> deleteDoctorById(@PathVariable String doctorId);
		
    @DeleteMapping("/clinic-admin/delete-doctors-by-clinic/{clinicId}")
    ResponseEntity<Response> deleteDoctorsByClinic(@PathVariable String clinicId); 
    
    
    

	
	///FALLBACK METHODS
	
	default ResponseEntity<?> clinicAdminServiceFallBack(Exception e){		 
		return ResponseEntity.status(503).body(new Response(false,null,"CLINIC ADMIN SERVICE NOT AVAILABLE",503,null,null, null, null, null));
		
	
	}

}

package com.clinicadmin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class DoctorLoginDTO {
	private String username;
	private String password;
	private String deviceId;
	private String staffId;
	private String hospitalId;

	public void setDoctorMobileNumber(String doctorMobileNumber) {
		this.username = username != null ? username.trim() : null;
	}

	public void setPassword(String password) {
		this.password = password != null ? password.trim() : null;
	}


	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId != null ? deviceId.trim() : null;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId != null ? staffId.trim() : null;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId != null ? hospitalId.trim() : null;
	}
}

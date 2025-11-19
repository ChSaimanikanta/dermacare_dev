package com.pharmacyManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacyManagement.dto.MedicineDetailDTO;
import com.pharmacyManagement.dto.Response;
import com.pharmacyManagement.service.MedicineDetailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/medicine")
@RequiredArgsConstructor
public class MedicineDetailController {

	@Autowired
    private  MedicineDetailService medicineDetailService;

    // CREATE MEDICINE
    @PostMapping("/addMedicine")
    public ResponseEntity<Response> addMedicine(@RequestBody MedicineDetailDTO dto) {
        Response response = medicineDetailService.saveMedicine(dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // GET MEDICINE BY ID
    @GetMapping("/getMedicineById/{id}")
    public ResponseEntity<Response> getMedicineById(@PathVariable String id) {
        Response response = medicineDetailService.getMedicineById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // GET ALL MEDICINES
    @GetMapping("/getAllMedicines")
    public ResponseEntity<Response> getAllMedicines() {
        Response response = medicineDetailService.getAllMedicine();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

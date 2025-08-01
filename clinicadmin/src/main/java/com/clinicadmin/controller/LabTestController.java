package com.clinicadmin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.clinicadmin.dto.LabTestDTO;
import com.clinicadmin.dto.Response;
import com.clinicadmin.service.LabTestService;

@RestController
@RequestMapping("/clinic-admin/labtest")
public class LabTestController {

    @Autowired
    private LabTestService labTestService;

    @PostMapping("/addLabTest")
    public ResponseEntity<Response> addLabTest(@RequestBody LabTestDTO dto) {
        Response response = labTestService.addLabTest(dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/getAllLabTests")
    public ResponseEntity<Response> getAllLabTests() {
        Response response = labTestService.getAllLabTests();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("getLabTestById/{id}/{hospitalId}")
    public ResponseEntity<Response> getLabTestById(@PathVariable String id, @PathVariable String hospitalId) {
        Response response = labTestService.getLabTestById(id, hospitalId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("deleteLabTest/{id}/{hospitalId}")
    public ResponseEntity<Response> deleteLabTest(@PathVariable String id,@PathVariable String hospitalId) {
        Response response = labTestService.deleteLabTestById(id,hospitalId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("updateLabTest/{id}/{hospitalId}")
    public ResponseEntity<Response> updateLabTest(@PathVariable String id,@PathVariable String hospitalId, @RequestBody LabTestDTO dto) {
        Response response = labTestService.updateLabTestById(id,hospitalId, dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

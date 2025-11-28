package com.clinicadmin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinicadmin.dto.CityDTO;
import com.clinicadmin.dto.Response;
import com.clinicadmin.service.CityService;

@RestController
@RequestMapping("/clinic-admin/city")
public class CityController {

	@Autowired
    private  CityService cityService;

    @PostMapping("/addCity")
    public Response saveCity(@RequestBody CityDTO dto) {
        return cityService.saveCity(dto);
    }

    @PutMapping("/updateByCityId/{id}")
    public Response updateCity(@PathVariable String id,
                               @RequestBody CityDTO dto) {
        return cityService.updateCity(id, dto);
    }

    @GetMapping("/getByCityId/{id}")
    public Response getByityById(@PathVariable String id) {
        return cityService.getCityById(id);
    }

    @GetMapping("/getAllCitiesall")
    public Response getAllCities() {
        return cityService.getAllCities();
    }

    @DeleteMapping("/deleteCityById/{id}")
    public Response deleteCityById(@PathVariable String id) {
        return cityService.deleteCity(id);
    }
}

package com.clinicadmin.feignclient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.clinicadmin.dto.AreaDTO;
import com.clinicadmin.dto.CityDTO;
import com.clinicadmin.dto.Response;

@FeignClient(name = "pharmacy-management")
public interface PharmacyManagementFeignClient {

    @PostMapping("/api/pharmacy/area/save")
    Response saveArea(@RequestBody AreaDTO dto);

    @PutMapping("/api/pharmacy/area/update/{id}")
    Response updateArea(@PathVariable String id, @RequestBody AreaDTO dto);

    @GetMapping("/api/pharmacy/area/getById/{id}")
    Response getAreaById(@PathVariable String id);

    @GetMapping("/api/pharmacy/area/all")
    Response getAllAreas();

    @DeleteMapping("/api/pharmacy/area/delete/{id}")
    Response deleteArea(@PathVariable String id);

    @GetMapping("/api/pharmacy/area/city/{cityId}")
    Response getAreasByCity(@PathVariable String cityId);
    
    //// ========== CITY APIs ========== ////    

    // SAVE CITY
    @PostMapping("/api/pharmacy/city/save")
    Response saveCity(@RequestBody CityDTO dto);

    // UPDATE CITY
    @PutMapping("/api/pharmacy/city/update/{id}")
    Response updateCity(@PathVariable String id, @RequestBody CityDTO dto);

    // GET CITY BY ID
    @GetMapping("/api/pharmacy/city/getById/{id}")
    Response getCityById(@PathVariable String id);

    // GET ALL CITIES
    @GetMapping("/api/pharmacy/city/all")
    Response getAllCities();

    // DELETE CITY
    @DeleteMapping("/api/pharmacy/city/delete/{id}")
    Response deleteCity(@PathVariable String id);
}
    
    
    
    


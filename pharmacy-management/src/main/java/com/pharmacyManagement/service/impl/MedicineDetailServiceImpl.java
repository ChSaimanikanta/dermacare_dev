package com.pharmacyManagement.service.impl;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pharmacyManagement.dto.MedicineDetailDTO;
import com.pharmacyManagement.dto.Response;
import com.pharmacyManagement.entity.MedicineDetail;
import com.pharmacyManagement.repository.MedicineDetailRepository;
import com.pharmacyManagement.service.MedicineDetailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicineDetailServiceImpl implements MedicineDetailService {

    private final MedicineDetailRepository repository;

    // ---------------------------------------------------------
    // SAVE MEDICINE (WITH DUPLICATE NAME CHECK)
    // ---------------------------------------------------------
    @Override
    public Response saveMedicine(MedicineDetailDTO dto) {

        Response response = new Response();

        // Check duplicate product name
        MedicineDetail existing = repository.findByProductName(dto.getProductName());
        if (existing != null) {
            response.setSuccess(false);
            response.setMessage("Medicine name already exists: " + dto.getProductName());
            response.setStatus(HttpStatus.CONFLICT.value());
            return response;
        }

        MedicineDetail entity = mapToEntity(dto);
        repository.save(entity);

        response.setSuccess(true);
        response.setMessage("Medicine saved successfully");
        response.setData(mapToDTO(entity));
        response.setStatus(HttpStatus.CREATED.value());

        return response;
    }

    // ---------------------------------------------------------
    // GET MEDICINE BY ID
    // ---------------------------------------------------------
    @Override
    public Response getMedicineById(String id) {

        Response response = new Response();

        MedicineDetail entity = repository.findById(id).orElse(null);

        if (entity == null) {
            response.setSuccess(false);
            response.setMessage("Medicine not found");
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return response;
        }

        response.setSuccess(true);
        response.setMessage("Medicine retrieved successfully");
        response.setData(mapToDTO(entity));
        response.setStatus(HttpStatus.OK.value());

        return response;
    }

    // ---------------------------------------------------------
    // GET ALL MEDICINES
    // ---------------------------------------------------------
    @Override
    public Response getAllMedicine() {

        Response response = new Response();

        response.setSuccess(true);
        response.setMessage("All medicines fetched");
        response.setData(
                repository.findAll().stream()
                        .map(this::mapToDTO)
                        .collect(Collectors.toList())
        );
        response.setStatus(HttpStatus.OK.value());

        return response;
    }

    // ---------------------------------------------------------
    // MAPPERS
    // ---------------------------------------------------------
    private MedicineDetail mapToEntity(MedicineDetailDTO dto) {

        MedicineDetail entity = new MedicineDetail();

        entity.setId(dto.getId());
        entity.setProductName(dto.getProductName());
        entity.setBatchNo(dto.getBatchNo());
        entity.setExpDate(dto.getExpDate());
        entity.setQuantity(dto.getQuantity());
        entity.setPackSize(dto.getPackSize());
        entity.setFree(dto.getFree());
        entity.setGstPercent(dto.getGstPercent());
        entity.setCostPrice(dto.getCostPrice());
        entity.setMrp(dto.getMrp());
        entity.setDiscPercent(dto.getDiscPercent());

        return entity;
    }

    private MedicineDetailDTO mapToDTO(MedicineDetail entity) {

        MedicineDetailDTO dto = new MedicineDetailDTO();

        dto.setId(entity.getId());
        dto.setProductName(entity.getProductName());
        dto.setBatchNo(entity.getBatchNo());
        dto.setExpDate(entity.getExpDate());
        dto.setQuantity(entity.getQuantity());
        dto.setPackSize(entity.getPackSize());
        dto.setFree(entity.getFree());
        dto.setGstPercent(entity.getGstPercent());
        dto.setCostPrice(entity.getCostPrice());
        dto.setMrp(entity.getMrp());
        dto.setDiscPercent(entity.getDiscPercent());

        return dto;
    }
}

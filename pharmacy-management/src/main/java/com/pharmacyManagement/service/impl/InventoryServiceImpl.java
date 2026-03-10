package com.pharmacyManagement.service.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pharmacyManagement.dto.InventoryResponseDTO;
import com.pharmacyManagement.dto.Response;
import com.pharmacyManagement.entity.Inventory;
import com.pharmacyManagement.repository.InventoryRepository;
import com.pharmacyManagement.service.InventoryService;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public Response createInventory(InventoryResponseDTO dto) {

        Inventory inventory = new Inventory();

        inventory.setMedicineId(dto.getMedicineId());
        inventory.setMedicineName(dto.getMedicineName());
        inventory.setBatchNo(dto.getBatchNo());
        inventory.setMfgDate(dto.getMfgDate());
        inventory.setExpiryDate(dto.getExpiryDate());
        inventory.setAvailableQty(dto.getAvailableQty());
        inventory.setMinStock(dto.getMinStock());
        inventory.setPurchaseRate(dto.getPurchaseRate());
        inventory.setMrp(dto.getMrp());
        inventory.setGstPercent(dto.getGstPercent());
        inventory.setSupplierId(dto.getSupplierId());
        inventory.setSupplier(dto.getSupplier());

<<<<<<< Updated upstream
	    inventory.setProductId(dto.getMedicineId());
	    inventory.setProductName(dto.getMedicineName());
	    inventory.setBatchNo(dto.getBatchNo());
	    inventory.setMfgDate(dto.getMfgDate());
	    inventory.setExpiryDate(dto.getExpiryDate());
	    inventory.setAvailableQty(dto.getAvailableQty());
	    inventory.setMinStock(dto.getMinStock());
	    inventory.setPurchaseRate(dto.getPurchaseRate());
	    inventory.setMrp(dto.getMrp());
	    inventory.setGstPercent(dto.getGstPercent());
	    inventory.setSupplierId(dto.getSupplier());
	    inventory.setClinicId(dto.getClinicId());
	    inventory.setBranchId(dto.getBranchId());
=======
        inventory.setClinicId(dto.getClinicId());
        inventory.setBranchId(dto.getBranchId());
>>>>>>> Stashed changes

        inventory.setStatus("ACTIVE");

        Inventory saved = inventoryRepository.save(inventory);

        return Response.builder()
                .success(true)
                .data(saved)
                .message("Inventory created successfully")
                .status(HttpStatus.OK.value())
                .build();
    }

    @Override
    public Response getInventoryById(String inventoryId) {

        Optional<Inventory> optional = inventoryRepository.findById(inventoryId);

        if (optional.isEmpty()) {

            return Response.builder()
                    .success(false)
                    .message("Inventory not found")
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }

        InventoryResponseDTO dto = mapToDTO(optional.get());

        return Response.builder()
                .success(true)
                .data(dto)
                .message("Inventory fetched successfully")
                .status(HttpStatus.OK.value())
                .build();
    }

<<<<<<< Updated upstream
	    dto.setMedicineId(inv.getProductId());
	    dto.setMedicineName(inv.getProductName());
	    dto.setBatchNo(inv.getBatchNo());
	    dto.setMfgDate(inv.getMfgDate());
	    dto.setExpiryDate(inv.getExpiryDate());
	    dto.setAvailableQty(inv.getAvailableQty());
	    dto.setMinStock(inv.getMinStock());
	    dto.setPurchaseRate(inv.getPurchaseRate());
	    dto.setMrp(inv.getMrp());
	    dto.setGstPercent(inv.getGstPercent());
	    dto.setSupplier(inv.getSupplierId());
	    dto.setClinicId(inv.getClinicId());
	    dto.setBranchId(inv.getBranchId());

	    try {

	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	        LocalDate expiryDate = LocalDate.parse(inv.getExpiryDate(), formatter);

	        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);

	        dto.setDaysLeft(daysLeft);

	        if (daysLeft <= 0) {
	            dto.setStatus("EXPIRED");
	        } else if (daysLeft <= 30) {
	            dto.setStatus("NEAR_EXPIRY");
	        } else {
	            dto.setStatus("ACTIVE");
	        }

	    } catch (Exception e) {

	        dto.setDaysLeft(0);
	        dto.setStatus("INVALID_DATE");
	    }
=======
    @Override
    public Response getAllInventory(String clinicId, String branchId) {

        List<InventoryResponseDTO> list = inventoryRepository
                .findByClinicIdAndBranchId(clinicId, branchId)
                .stream()
                .map(this::mapToDTO)
                .toList();

        return Response.builder()
                .success(true)
                .data(list)
                .message("Inventory fetched successfully")
                .status(HttpStatus.OK.value())
                .build();
    }

    @Override
    public Response updateInventory(String inventoryId, InventoryResponseDTO dto) {
>>>>>>> Stashed changes

        Optional<Inventory> optional = inventoryRepository.findById(inventoryId);

        if (optional.isEmpty()) {

            return Response.builder()
                    .success(false)
                    .message("Inventory not found")
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }

        Inventory inv = optional.get();

        inv.setMedicineName(dto.getMedicineName());
        inv.setBatchNo(dto.getBatchNo());
        inv.setMfgDate(dto.getMfgDate());
        inv.setExpiryDate(dto.getExpiryDate());
        inv.setAvailableQty(dto.getAvailableQty());
        inv.setMinStock(dto.getMinStock());
        inv.setPurchaseRate(dto.getPurchaseRate());
        inv.setMrp(dto.getMrp());

        Inventory updated = inventoryRepository.save(inv);

        return Response.builder()
                .success(true)
                .data(updated)
                .message("Inventory updated successfully")
                .status(HttpStatus.OK.value())
                .build();
    }

<<<<<<< Updated upstream
	    inv.setProductName(dto.getMedicineName());
	    inv.setBatchNo(dto.getBatchNo());
	    inv.setMfgDate(dto.getMfgDate());
	    inv.setExpiryDate(dto.getExpiryDate());
	    inv.setAvailableQty(dto.getAvailableQty());
	    inv.setMinStock(dto.getMinStock());
	    inv.setPurchaseRate(dto.getPurchaseRate());
	    inv.setMrp(dto.getMrp());
	    inv.setGstPercent(dto.getGstPercent());
	    inv.setSupplierId(dto.getSupplier());
	    inv.setClinicId(dto.getClinicId());
=======
    @Override
    public Response deleteInventory(String inventoryId) {
>>>>>>> Stashed changes

        inventoryRepository.deleteById(inventoryId);

        return Response.builder()
                .success(true)
                .message("Inventory deleted successfully")
                .status(HttpStatus.OK.value())
                .build();
    }

    private InventoryResponseDTO mapToDTO(Inventory inv) {

        InventoryResponseDTO dto = new InventoryResponseDTO();

        dto.setInventoryId(inv.getInventoryId());
        dto.setMedicineId(inv.getMedicineId());
        dto.setMedicineName(inv.getMedicineName());

        dto.setBatchNo(inv.getBatchNo());
        dto.setMfgDate(inv.getMfgDate());
        dto.setExpiryDate(inv.getExpiryDate());

        dto.setAvailableQty(inv.getAvailableQty());
        dto.setMinStock(inv.getMinStock());

        dto.setPurchaseRate(inv.getPurchaseRate());
        dto.setMrp(inv.getMrp());
        dto.setGstPercent(inv.getGstPercent());

        dto.setSupplierId(inv.getSupplierId());
        dto.setSupplier(inv.getSupplier());

        dto.setClinicId(inv.getClinicId());
        dto.setBranchId(inv.getBranchId());

        LocalDate expiry = LocalDate.parse(inv.getExpiryDate());

<<<<<<< Updated upstream
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	    List<InventoryResponseDTO> inventoryList = inventoryRepository.findAll()
	            .stream()
	            .map(inv -> {
=======
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), expiry);
>>>>>>> Stashed changes

        dto.setDaysLeft(daysLeft);

<<<<<<< Updated upstream
	                dto.setMedicineId(inv.getProductId());
	                dto.setMedicineName(inv.getProductName());
	                dto.setBatchNo(inv.getBatchNo());
	                dto.setMfgDate(inv.getMfgDate());
	                dto.setExpiryDate(inv.getExpiryDate());
	                dto.setAvailableQty(inv.getAvailableQty());
	                dto.setMinStock(inv.getMinStock());
	                dto.setPurchaseRate(inv.getPurchaseRate());
	                dto.setMrp(inv.getMrp());
	                dto.setGstPercent(inv.getGstPercent());
	                dto.setSupplier(inv.getSupplierId());
	                dto.setClinicId(inv.getClinicId());
	                dto.setBranchId(inv.getBranchId());

	                try {
	                    // Expiry Calculation
	                    LocalDate expiry = LocalDate.parse(inv.getExpiryDate(), formatter);
	                    long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), expiry);

	                    dto.setDaysLeft(daysLeft);

	                    if (daysLeft <= 0) {
	                        dto.setStatus("EXPIRED");
	                    } else if (daysLeft <= 30) {
	                        dto.setStatus("NEAR_EXPIRY");
	                    } else {
	                        dto.setStatus("ACTIVE");
	                    }

	                } catch (Exception e) {
	                    dto.setDaysLeft(0);
	                    dto.setStatus("INVALID_DATE");
	                }
=======
        if (daysLeft <= 0)
            dto.setStatus("EXPIRED");
        else if (daysLeft <= 30)
            dto.setStatus("NEAR_EXPIRY");
        else
            dto.setStatus("ACTIVE");

        return dto;
    }
    @Override
    public Response deleteInventory(String medicineId, String batchNo) {

        log.info("Deleting inventory for medicineId {} batch {}", medicineId, batchNo);

        Inventory inventory =
                inventoryRepository.findByMedicineIdAndBatchNo(medicineId, batchNo);
>>>>>>> Stashed changes

        if (inventory == null) {

            return Response.builder()
                    .success(false)
                    .message("Inventory not found for medicineId: " + medicineId + " batch: " + batchNo)
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }

        inventoryRepository.delete(inventory);

<<<<<<< Updated upstream
	    return res;
	}
	@Override
	public Response deleteInventory(String medicineId, String batchNo) {

	    log.info("Deleting inventory for medicineId {} and batch {}", medicineId, batchNo);

	    Response res = new Response();

	    Inventory inventory = inventoryRepository.findByProductIdAndBatchNo(medicineId, batchNo);

	    if (inventory == null) {

	        res.setSuccess(false);
	        res.setMessage("Inventory not found for medicineId: " + medicineId + " and batchNo: " + batchNo);
	        res.setStatus(HttpStatus.NOT_FOUND.value());
	        return res;
	    }

	    inventoryRepository.deleteByProductIdAndBatchNo(medicineId, batchNo);

	    res.setSuccess(true);
	    res.setMessage("Inventory deleted successfully");
	    res.setStatus(HttpStatus.OK.value());

	    return res;
	}
	@Override
	public Response getInventoryByClinicAndBranch(String clinicId, String branchId) {

	    List<Inventory> list = inventoryRepository.findByClinicIdAndBranchId(clinicId, branchId);

	    List<InventoryResponseDTO> dtoList = list.stream().map(inv -> {

	        InventoryResponseDTO dto = new InventoryResponseDTO();

	        dto.setMedicineId(inv.getProductId());
	        dto.setMedicineName(inv.getProductName());
	        dto.setBatchNo(inv.getBatchNo());
	        dto.setMfgDate(inv.getMfgDate());
	        dto.setExpiryDate(inv.getExpiryDate());
	        dto.setAvailableQty(inv.getAvailableQty());
	        dto.setMinStock(inv.getMinStock());
	        dto.setPurchaseRate(inv.getPurchaseRate());
	        dto.setMrp(inv.getMrp());
	        dto.setGstPercent(inv.getGstPercent());
	        dto.setSupplier(inv.getSupplierId());

	        dto.setClinicId(inv.getClinicId());
	        dto.setBranchId(inv.getBranchId());

	        return dto;

	    }).toList();

	    Response res = new Response();
	    res.setSuccess(true);
	    res.setData(dtoList);
	    res.setMessage("Inventory fetched successfully");
	    res.setStatus(200);

	    return res;
	}
=======
        return Response.builder()
                .success(true)
                .message("Inventory deleted successfully")
                .status(HttpStatus.OK.value())
                .build();
    }
>>>>>>> Stashed changes
}
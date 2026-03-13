package com.pharmacyManagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryResponseDTO {

    private String medicineId;
    private String medicineName;

    private String batchNo;
    private String mfgDate;
    private String expiryDate;
    private long daysLeft;

    private double availableQty;
    private double minStock;

    private double purchaseRate;
    private double mrp;
    private double gstPercent;

    private String supplier;
    private String status;
}
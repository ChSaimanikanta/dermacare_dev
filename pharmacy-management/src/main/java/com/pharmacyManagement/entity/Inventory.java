package com.pharmacyManagement.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "inventory")
public class Inventory {

    @Id
    private String inventoryId;

    private String medicineId;
    private String medicineName;
    
    private String brand;
    private String productType;
    private String pack;
    
    private String batchNo;
    private String mfgDate;
    private String expiryDate;
    
//    private String daysLeft;
    
    private double availableQty;
    private double minStock;
    
    private double purchaseRate;
    private double mrp;
    private double gstPercent;
    
    private String supplierId;
<<<<<<< Updated upstream
=======
    private String supplier;
    
    private String status;
    // ✅ Multi tenant fields
>>>>>>> Stashed changes
    private String clinicId;
    private String branchId;
    
}
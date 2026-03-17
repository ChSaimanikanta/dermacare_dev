package com.pharmacyManagement.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pharmacyManagement.dto.DoctorSaveDetailsDTO;
import com.pharmacyManagement.dto.OpMedicineDTO;
import com.pharmacyManagement.dto.OpNoResponse;
import com.pharmacyManagement.dto.OpSalesRequest;
import com.pharmacyManagement.dto.OpSalesResponse;
import com.pharmacyManagement.dto.Response;
import com.pharmacyManagement.entity.Inventory;
import com.pharmacyManagement.entity.Medicine;
import com.pharmacyManagement.entity.OpMedicine;
import com.pharmacyManagement.entity.OpSales;
import com.pharmacyManagement.entity.PaymentEntry;
import com.pharmacyManagement.feign.DoctorFeign;
import com.pharmacyManagement.repository.InventoryRepository;
import com.pharmacyManagement.repository.MedicineRepository;
import com.pharmacyManagement.repository.OpSalesRepository;
import com.pharmacyManagement.service.Opservice;
import com.pharmacyManagement.util.DuplicateBillNoException;
import com.pharmacyManagement.util.ResourceNotFoundException;
import com.pharmacyManagement.util.ValidationException;

@Service
public class OpserviceImpl implements Opservice {

    @Autowired
    private OpSalesRepository opSalesRepository;
    
    @Autowired
    private DoctorFeign doctorFeign;
    
    @Autowired
    private MedicineRepository inventoryRepository;

    // ── FIX: inject ObjectMapper as a Spring bean instead of using new ObjectMapper() ──
    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger log = LoggerFactory.getLogger(OpserviceImpl.class);

    //// GET ALL
    // =========================================================================
    
    @Override
    public ResponseEntity<Response> createOpSales(OpSalesRequest request){
    	Response res = new Response();
    	OpSales opsale = null;
    	try {
    		OpSales opsales = opSalesRepository.findByBillNo(request.getBillNo()).get();
    		if(opsales != null) {
    			res.setMessage("Bill Number already exist");
    			res.setStatus(409);
    			res.setSuccess(false);
    		}else {
    		List<OpMedicine> lst = validateMedicines(request.getMedicines());  
    		opsale = calculateValues(lst,request);
    		opsale.setMedicines(lst);
    		opSalesRepository.save(opsale);
    		res.setMessage("Opsales saved successfully");
			res.setStatus(200);
			res.setSuccess(true);
    		}}catch(Exception e) {}
    	return ResponseEntity.status(res.getStatus()).body(res);
    }
     
   
    public ResponseEntity<Response> updateOpSales(OpSalesRequest request){
    	Response res = new Response();
    	OpSales opsale = null;
    	try {
    		OpSales opsales = opSalesRepository.findByBillNo(request.getBillNo()).get();
    		if(opsales != null) {
    			if (request.getBillNo() != null || !request.getBillNo().isBlank()) {
    				opsales.setBillNo(request.getBillNo());
    		    }
    		    // ── billDate ─────────────────────────────────────────────────────────────
    		    if (request.getBillDate() != null || !request.getBillDate().isBlank()) {
    		    	opsales.setBillDate(request.getBillDate());
    		    }

    		    // ── billTime ─────────────────────────────────────────────────────────────
    		    if (request.getBillTime() != null || !request.getBillTime().isBlank()) {
    		    	opsales.setBillTime(request.getBillTime());
    		    }

    		    // ── patientName ──────────────────────────────────────────────────────────
    		    if (request.getPatientName() != null || !request.getPatientName().isBlank()) {
    		    	opsales.setPatientName(request.getPatientName());
    		    }

    		    // ── includeReturns ───────────────────────────────────────────────────────
    		    if (request.getIncludeReturns() != null) {
    		    	opsales.setIncludeReturns(request.getIncludeReturns());
    		    }

    		    // ── medicines ────────────────────────────────────────────────────────────
    		    if (request.getMedicines() != null || !request.getMedicines().isEmpty()) {
    		    	List<OpMedicine> lst = validateMedicines(request.getMedicines());
    		    	List<OpMedicine> reqList = opsales.getMedicines();
    		    	reqList.addAll(lst);
    		    	opsales.setMedicines(reqList);
    		    }
   		    
    		    // ── clinicId ─────────────────────────────────────────────────────────────
    		    if (request.getClinicId() != null || !request.getClinicId().isBlank()) {
    		    	opsales.setClinicId(request.getClinicId() );
    		    }

    		    // ── branchId ─────────────────────────────────────────────────────────────
    		    if (request.getBranchId() != null || !request.getBranchId().isBlank()) {
    		    	opsales.setBranchId(request.getBranchId());
    		    	OpSales opsle = calculateAndUpdateValues(request.getAmountPaid(),opsales);
    		    	if(opsle != null) {
    		    		opSalesRepository.save(opsale);
    		    		res.setMessage("Opsales updated successfully");
    					res.setStatus(200);
    					res.setSuccess(true);
    		        }}}else {  		 
    		res.setMessage("Opsale Not Found to Update");
			res.setStatus(404);
			res.setSuccess(false);
    		}}catch(Exception e) {}
    	return ResponseEntity.status(res.getStatus()).body(res);
    }
        
    @Override
    public ResponseEntity<Response> getAllOpSales(String clinicId, String branchId) {
        Response res = new Response();
        log.info("Fetching all OP Sales for clinicId: {}, branchId: {}", clinicId, branchId);

        List<OpSales> lst = opSalesRepository
                .findByClinicIdAndBranchId(clinicId, branchId);
        List<OpSalesResponse> response = objectMapper.convertValue(lst, new TypeReference<List<OpSalesResponse>>() {
		});
                
        if (!response.isEmpty()) {
            res.setMessage("OP Sales retrieved successfully");
            res.setStatus(200);
            res.setData(response);
            res.setSuccess(true);
        } else {
            res.setMessage("Unable to retrieve OP Sales");
            res.setStatus(404);
            res.setSuccess(false);
        }
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    // =========================================================================
    //  4a. GET BY BILL NO
    // =========================================================================
    @Override
    public ResponseEntity<Response> getByBillNo(String billNo) {
        Response res = new Response();
        log.info("Fetching OP Sales by billNo: {}", billNo);

        OpSales ops = opSalesRepository.findByBillNo(billNo)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "OP Sales not found with billNo: " + billNo));
        OpSalesResponse response = objectMapper.convertValue(ops, OpSalesResponse.class);
        if (response != null) {
            res.setMessage("OP Sales retrieved successfully");
            res.setStatus(200);
            res.setData(ops);
            res.setSuccess(true);
        } else {
            res.setMessage("Unable to retrieve OP Sales");
            res.setStatus(404);
            res.setSuccess(false);
        }
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    // =========================================================================
    //  4b. GET BY ID
    // =========================================================================
    @Override
    public ResponseEntity<Response> getById(String id) {
        Response res = new Response();
        log.info("Fetching OP Sales by id: {}", id);

        OpSales ops = opSalesRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "OP Sales not found with id: " + id));
        OpSalesResponse response = objectMapper.convertValue(ops, OpSalesResponse.class);
        
        if (response != null) {
            res.setMessage("OP Sales retrieved successfully");
            res.setStatus(200);
            res.setData(ops);
            res.setSuccess(true);
        } else {
            res.setMessage("Unable to retrieve OP Sales");
            res.setStatus(404);
            res.setSuccess(false);
        }
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    // =========================================================================
    //  5. GET BY OPNO
    // =========================================================================
    @Override
    public ResponseEntity<Response> getByOpNo(String clinicId, String branchId, String opNo) {
        Response res = new Response();
        log.info("Fetching by opNo: {}, clinicId: {}, branchId: {}", opNo, clinicId, branchId);
        try {
        DoctorSaveDetailsDTO doctorSaveDetailsDTO = doctorFeign.getDoctorSaveDetails(opNo).getBody();
        //System.out.println(doctorSaveDetailsDTO);
        List<String> lst = doctorSaveDetailsDTO.getPrescription().getMedicines().stream().map(n->n.getId()).toList();
        List<Medicine> lt = new ArrayList<>();
        for(String s : lst) {
         Medicine invent = inventoryRepository.findById(s).get();
        lt.add(invent);}
        List<OpSales> lsts = opSalesRepository.findByClinicIdAndBranchIdAndOpNo(clinicId, branchId, opNo);
        OpSales opSales = lsts.get(lsts.size()-1);
        OpNoResponse opNoResponse = new OpNoResponse();
        opNoResponse.setAge(opSales.getAge());
        opNoResponse.setBranchId(opSales.getBranchId());
        opNoResponse.setClinicId(opSales.getClinicId());
        opNoResponse.setMedicines(lt);
        opNoResponse.setMobile(opSales.getMobile());
        opNoResponse.setOpNo(opSales.getOpNo());
        opNoResponse.setPatientName(opSales.getPatientName());
        opNoResponse.setSex(opSales.getSex());
        opNoResponse.setVisitType(opSales.getVisitType());
        if(opNoResponse == null) {
            res.setMessage("OP Sales Not Found with OpNo");
            res.setStatus(404);
            res.setSuccess(false);
        }else{       
        res.setMessage("OP Sales Found with OpNo");
        res.setStatus(200);
        res.setData(lt);
        res.setSuccess(true);
        }}catch(Exception e) {}
        return ResponseEntity.status(res.getStatus()).body(res);}
  

    // =========================================================================
    //  6. DELETE
    // =========================================================================
    @Override
    public ResponseEntity<Response> deleteOpSales(String clinicId, String branchId, String id) {
        Response res = new Response();
        log.info("Deleting OP Sales id: {}, clinicId: {}, branchId: {}", id, clinicId, branchId);

        OpSales opSales = opSalesRepository
                .findByIdAndClinicIdAndBranchId(id, clinicId, branchId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "OP Sales not found with id: " + id + " for clinic/branch"));

        opSalesRepository.delete(opSales);
        res.setMessage("OP Sales Deleted Successfully");
        res.setStatus(200);
        res.setSuccess(true);
        log.info("OP Sales deleted with id: {}", id);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    // =========================================================================
    //  7. FILTER
    // =========================================================================
    @Override
    public ResponseEntity<Response> filterOpSales(String clinicId, String branchId,
            String billNo, String patientName,
            String mobile, String consultingDoctor,
            String fromDate, String toDate) {

        Response res = new Response();
        log.info("Filtering OP Sales for clinicId: {}, branchId: {}", clinicId, branchId);

        List<OpSales> rawList = opSalesRepository
                .findByClinicIdAndBranchIdAndBillNoContainingIgnoreCaseAndPatientNameContainingIgnoreCaseAndMobileAndConsultingDoctorContainingIgnoreCase(
                        clinicId,
                        branchId,
                        billNo,
                        patientName,
                        mobile,
                        consultingDoctor);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate from = isPresent(fromDate) ? LocalDate.parse(fromDate, formatter) : null;
        LocalDate to   = isPresent(toDate)   ? LocalDate.parse(toDate,   formatter) : null;

        List<OpSales> list = rawList.stream()
                .filter(sale -> {
                    if (sale.getBillDate() == null) return false;
                    LocalDate billDate = LocalDate.parse(sale.getBillDate(), formatter);
                    if (from != null && billDate.isBefore(from)) return false;
                    if (to   != null && billDate.isAfter(to))    return false;
                    return true;
                }).collect(Collectors.toList());
        
        List<OpSalesResponse> lst = objectMapper.convertValue(list,new TypeReference< List<OpSalesResponse>>() {
		});
        if (!lst.isEmpty()) {
            res.setMessage("OP Sales retrieved successfully");
            res.setStatus(200);
            res.setData(lst);
            res.setSuccess(true);
        } else {
            res.setMessage("Unable to retrieve OP Sales");
            res.setStatus(404);
            res.setSuccess(false);
        }
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    private boolean isPresent(String val) {
        return val != null && !val.isBlank();
    }
    
    
    public List<OpMedicine> validateMedicines(List<OpMedicineDTO> dto){
    	List<OpMedicineDTO> lst = dto.stream().map(d->{
    		Double totalA = d.getRate() * d.getQty();
    		d.setTotalA(totalA);
    		Double netAmtAB = d.getTotalA() - d.getDiscAmtB();
    		d.setNetAmtAB(netAmtAB);
    		d.setFinalAmountABC(netAmtAB);
    	return d;}).toList();
    	List<OpMedicine> opMedicineList = objectMapper.convertValue(lst, new TypeReference<List<OpMedicine>>() {
		});
    	return opMedicineList;
    }
    
    public OpSales calculateValues(List<OpMedicine> dto,OpSalesRequest request) {
    OpSales opsales = new ObjectMapper().convertValue(request, OpSales.class);
    	Double totalAmt = 0.0;
    	double totalDiscPct = 0.0;
    	double totalDiscAmt = 0.0;
    	for(OpMedicine o : dto) {
    		totalAmt += o.getTotalA();
    		totalDiscPct += o.getDiscPercent();
    		totalDiscAmt += Math.abs(o.getDiscAmtB());
    	}
    	double avgDiscPercent = Math.round(totalDiscPct / dto.size()*100.0/100.0); // average disc %
    	Double netAmount = totalAmt - totalDiscAmt;
    	opsales.setTotalAmt(totalAmt);
    	opsales.setTotalDiscAmt(totalDiscAmt);
        opsales.setAvgDiscPercent(avgDiscPercent);
        opsales.setNetAmount(netAmount);
        opsales.setFinalTotal(netAmount);
        opsales.setMedicines(dto);
        opsales.setCurrentPaymentAmount(request.getAmountPaid());
        opsales.setAlreadyPaidAmount(0.0);
        opsales.setTotalPaidAmount(opsales.getAlreadyPaidAmount()+ opsales.getCurrentPaymentAmount());
        opsales.setDueAmount(netAmount - opsales.getTotalPaidAmount());
        PaymentEntry paymentEntry = new PaymentEntry();
        paymentEntry.setAmountPaid(request.getAmountPaid());
        paymentEntry.setAlreadyPaid(0.0);
        paymentEntry.setTotalPaidSoFar(paymentEntry.getAmountPaid() + paymentEntry.getAlreadyPaid());
        paymentEntry.setDueAmount(netAmount - paymentEntry.getTotalPaidSoFar());
        List<PaymentEntry> pEntry = new ArrayList<>();
        pEntry.add(paymentEntry);
        opsales.setPaymentHistory(pEntry);
    	return opsales;
    }
    
    public OpSales calculateAndUpdateValues(double amountPaid, OpSales opsales ) {
         	Double totalAmt = 0.0;
        	double totalDiscPct = 0.0;
        	double totalDiscAmt = 0.0;
        	for(OpMedicine o : opsales.getMedicines()) {
        		totalAmt += o.getTotalA();
        		totalDiscPct += o.getDiscPercent();
        		totalDiscAmt += Math.abs(o.getDiscAmtB());
        	}
        	double avgDiscPercent = Math.round(totalDiscPct/opsales.getMedicines().size()*100.0/100.0); // average disc %
        	Double netAmount = totalAmt - totalDiscAmt;
        	opsales.setTotalAmt(totalAmt);
        	opsales.setTotalDiscAmt(totalDiscAmt);
            opsales.setAvgDiscPercent(avgDiscPercent);
            opsales.setNetAmount(netAmount);
            opsales.setFinalTotal(netAmount);
            opsales.setCurrentPaymentAmount(amountPaid);
           // opsales.setAlreadyPaidAmount());
            opsales.setTotalPaidAmount(amountPaid + opsales.getAlreadyPaidAmount());
            opsales.setDueAmount(netAmount - opsales.getTotalPaidAmount());
            PaymentEntry paymentEntry = new PaymentEntry();
            paymentEntry.setAmountPaid(amountPaid);
            paymentEntry.setAlreadyPaid(opsales.getAlreadyPaidAmount());
            paymentEntry.setTotalPaidSoFar(amountPaid + paymentEntry.getAlreadyPaid());
            paymentEntry.setDueAmount(netAmount - opsales.getTotalPaidAmount());
            List<PaymentEntry> pEntry = opsales.getPaymentHistory();
            pEntry.add(paymentEntry);
            opsales.setPaymentHistory(pEntry);
        	return opsales;
        }
        
}
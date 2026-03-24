package com.pharmacyManagement.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pharmacyManagement.dto.OrderDTO;
import com.pharmacyManagement.dto.ProductDTO;
import com.pharmacyManagement.dto.Response;
import com.pharmacyManagement.dto.StatusHistoryDTO;
import com.pharmacyManagement.entity.Order;
import com.pharmacyManagement.entity.Product;
import com.pharmacyManagement.entity.StatusHistory;
import com.pharmacyManagement.repository.OrderRepository;
import com.pharmacyManagement.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository repository;

    @Override
    public Response createOrder(OrderDTO dto) {

        Response response = new Response();

        try {

            // 🔥 Generate Order ID
            String orderId = "RO-" + System.currentTimeMillis();

            // 🔁 DTO → ENTITY
            Order order = mapToEntity(dto);
            order.setOrderId(orderId);

            // Save
            Order savedOrder = repository.save(order);

            // 🔁 ENTITY → DTO
            OrderDTO responseDto = mapToDTO(savedOrder);

            // ✅ Response
            response.setSuccess(true);
            response.setMessage("Order created successfully");
            response.setStatus(HttpStatus.CREATED.value());
            response.setData(responseDto);
            response.setUpdatedAt(LocalDateTime.now());

        } catch (Exception e) {

            response.setSuccess(false);
            response.setMessage("Failed to create order: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setUpdatedAt(LocalDateTime.now());
        }

        return response;
    }
    
    // ================= GET BY CLINIC + BRANCH =================
    @Override
    public Response getOrdersByClinicAndBranch(String clinicId, String branchId) {

        Response res = new Response();

        List<Order> orders = repository.findByClinicIdAndBranchId(clinicId, branchId);

        if (orders.isEmpty()) {
            res.setSuccess(false);
            res.setMessage("No orders found");
            res.setStatus(404);
            return res;
        }

        List<OrderDTO> dtoList = orders.stream()
                .map(this::mapToDTO)
                .toList();

        res.setSuccess(true);
        res.setData(dtoList);
        res.setStatus(200);
        return res;
    }
    
    @Override
    public Response getOrderByClinicBranchAndOrderId(String clinicId, String branchId, String orderId) {

        Response res = new Response();

        Optional<Order> optional =
                repository.findByClinicIdAndBranchIdAndOrderId(clinicId, branchId, orderId);

        if (optional.isEmpty()) {
            res.setSuccess(false);
            res.setMessage("Order not found");
            res.setStatus(404);
            return res;
        }

        res.setSuccess(true);
        res.setData(mapToDTO(optional.get()));
        res.setStatus(200);

        return res;
    }
    
    @Override
    public Response updateOrder(String orderId, OrderDTO dto) {

        Response res = new Response();

        Optional<Order> optional = repository.findByOrderId(orderId);

        if (optional.isEmpty()) {
            res.setSuccess(false);
            res.setMessage("Order not found");
            res.setStatus(404);
            return res;
        }

        Order order = optional.get();

        // 🔥 Manual update
        if (dto.getClinicId() != null) order.setClinicId(dto.getClinicId());
        if (dto.getClinicName() != null) order.setClinicName(dto.getClinicName());
        if (dto.getBranchId() != null) order.setBranchId(dto.getBranchId());
        if (dto.getBranchName() != null) order.setBranchName(dto.getBranchName());

        if (dto.getSupplierId() != null) order.setSupplierId(dto.getSupplierId());
        if (dto.getSupplierName() != null) order.setSupplierName(dto.getSupplierName());
        if (dto.getSupplierEmail() != null) order.setSupplierEmail(dto.getSupplierEmail());

        if (dto.getExpectedDeliveryDate() != null)
            order.setExpectedDeliveryDate(dto.getExpectedDeliveryDate());

        // ✅ STATUS HISTORY UPDATE (MAIN LOGIC 🔥)
        if (dto.getStatusHistory() != null && !dto.getStatusHistory().isEmpty()) {

            if (order.getStatusHistory() == null) {
                order.setStatusHistory(new ArrayList<>());
            }

            for (StatusHistoryDTO shDto : dto.getStatusHistory()) {

                StatusHistory sh = new StatusHistory();
                sh.setStatus(shDto.getStatus());
                sh.setTimestamp(
                        shDto.getTimestamp() != null
                                ? shDto.getTimestamp()
                                : Instant.now().toString()
                );

                // 👉 ADD new status (don't replace)
                order.getStatusHistory().add(sh);
            }
        }

        // Product update
        if (dto.getProducts() != null) {
            List<Product> products = dto.getProducts().stream().map(p -> {
                Product pr = new Product();
                pr.setProductId(p.getProductId());
                pr.setProductName(p.getProductName());
                pr.setHsnCode(p.getHsnCode());
                pr.setPackSize(p.getPackSize());
                pr.setQuantityRequested(p.getQuantityRequested());
                return pr;
            }).toList();

            order.setProducts(products);
        }

        repository.save(order);

        res.setSuccess(true);
        res.setMessage("Order updated successfully");
        res.setStatus(200);
        res.setData(mapToDTO(order));

        return res;
    }
    // ================= DELETE =================
    @Override
    public Response deleteOrder(String orderId) {

        Response res = new Response();

        Optional<Order> optional = repository.findByOrderId(orderId);

        if (optional.isEmpty()) {
            res.setSuccess(false);
            res.setMessage("Order not found");
            res.setStatus(404);
            return res;
        }

        repository.deleteByOrderId(orderId);

        res.setSuccess(true);
        res.setMessage("Order deleted successfully");
        res.setStatus(200);

        return res;
    }

    // ===============================
    // 🔁 DTO → ENTITY
    // ===============================
    private Order mapToEntity(OrderDTO dto) {

        Order order = new Order();
        order.setOrderId(dto.getOrderId());
        order.setClinicId(dto.getClinicId());
        order.setClinicName(dto.getClinicName());

        order.setBranchId(dto.getBranchId());
        order.setBranchName(dto.getBranchName());

        order.setSupplierId(dto.getSupplierId());
        order.setSupplierName(dto.getSupplierName());
        order.setSupplierEmail(dto.getSupplierEmail());

        order.setExpectedDeliveryDays(dto.getExpectedDeliveryDays());
        order.setExpectedDeliveryDate(dto.getExpectedDeliveryDate());

        // ✅ STATUS HISTORY (DEFAULT)
        List<StatusHistory> historyList = new ArrayList<>();

        if (dto.getStatusHistory() != null && !dto.getStatusHistory().isEmpty()) {

            historyList = dto.getStatusHistory().stream().map(h -> {
                StatusHistory sh = new StatusHistory();
                sh.setStatus(h.getStatus());
                sh.setTimestamp(
                        h.getTimestamp() != null
                                ? h.getTimestamp()
                                : Instant.now().toString()
                );
                return sh;
            }).toList();

        } else {
            // 👉 Default PENDING
            StatusHistory sh = new StatusHistory();
            sh.setStatus("PENDING");
            sh.setTimestamp(Instant.now().toString());
            historyList.add(sh);
        }

        order.setStatusHistory(historyList);

        // Product Mapping
        if (dto.getProducts() != null) {
            List<Product> productList = dto.getProducts().stream().map(p -> {
                Product product = new Product();
                product.setProductId(p.getProductId());
                product.setProductName(p.getProductName());
                product.setHsnCode(p.getHsnCode());
                product.setPackSize(p.getPackSize());
                product.setStatus("PENDING");
                product.setQuantityRequested(p.getQuantityRequested());
                return product;
            }).toList();

            order.setProducts(productList);
        }

        return order;
    }

    // ===============================
    // 🔁 ENTITY → DTO
    // ===============================
    private OrderDTO mapToDTO(Order order) {

        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setClinicId(order.getClinicId());
        dto.setClinicName(order.getClinicName());

        dto.setBranchId(order.getBranchId());
        dto.setBranchName(order.getBranchName());

        dto.setSupplierId(order.getSupplierId());
        dto.setSupplierName(order.getSupplierName());
        dto.setSupplierEmail(order.getSupplierEmail());

        dto.setExpectedDeliveryDays(order.getExpectedDeliveryDays());
        dto.setExpectedDeliveryDate(order.getExpectedDeliveryDate());

        // ✅ STATUS HISTORY
        if (order.getStatusHistory() != null) {
            List<StatusHistoryDTO> historyDTOList = order.getStatusHistory().stream().map(h -> {
                StatusHistoryDTO sh = new StatusHistoryDTO();
                sh.setStatus(h.getStatus());
                sh.setTimestamp(h.getTimestamp());
                return sh;
            }).toList();

            dto.setStatusHistory(historyDTOList);
        }

        // Product Mapping
        if (order.getProducts() != null) {
            List<ProductDTO> productDTOList = order.getProducts().stream().map(p -> {
                ProductDTO pdto = new ProductDTO();
                pdto.setProductId(p.getProductId());
                pdto.setProductName(p.getProductName());
                pdto.setHsnCode(p.getHsnCode());
                pdto.setPackSize(p.getPackSize());
                pdto.setStatus("PENDING");
                pdto.setQuantityRequested(p.getQuantityRequested());
                return pdto;
            }).toList();

            dto.setProducts(productDTOList);
        }

        return dto;
    }
}
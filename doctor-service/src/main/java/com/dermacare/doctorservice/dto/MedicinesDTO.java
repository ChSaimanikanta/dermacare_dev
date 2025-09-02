package com.dermacare.doctorservice.dto;

import java.util.List;

import com.dermacare.doctorservice.model.MedicineType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicinesDTO {
    private String id;
    private String name;
    private String dose;
    private String duration;
    private String food;
    private MedicineType medicineType;
    private String note;
    private String remindWhen;
    private List<String> times;
}

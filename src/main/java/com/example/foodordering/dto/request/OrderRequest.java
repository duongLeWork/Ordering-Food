package com.example.foodordering.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRequest {
    private Integer customerId;
    private BigDecimal totalAmount;
}

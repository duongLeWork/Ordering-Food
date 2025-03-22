package com.example.foodordering.dto.request;

import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    private Integer orderId;
    private String newStatus;
}

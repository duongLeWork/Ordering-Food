package com.example.foodordering.dto.request;

import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    private int orderId;
    private String newStatus;
}

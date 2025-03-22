package com.example.foodordering.dto.request;

import lombok.Data;

@Data
public class CartItemRequest {
    private Integer customerId;
    private Integer foodId;
    private Integer quantity;
}

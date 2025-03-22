package com.example.foodordering.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FoodRequest {
    private String name;
    private BigDecimal price;
    private String description;
    private String image;
    private Boolean isAvailable;
}

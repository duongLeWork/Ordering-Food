package com.example.foodordering.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FoodRequest {
    @NotBlank(message = "Keyword must not be empty")
    private String keyword;

    private String sortBy;

    private String category;

    private Integer maxResults;
}

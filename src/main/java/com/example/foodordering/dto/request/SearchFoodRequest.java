package com.example.foodordering.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SearchFoodRequest {
    @NotBlank(message = "Keyword must not be empty")
    private String keyword;

    private String sortBy;

    private String category;

    private Integer maxResults;
}

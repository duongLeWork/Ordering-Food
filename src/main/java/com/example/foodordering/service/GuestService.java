package com.example.foodordering.service;

import com.example.foodordering.dto.request.SearchFoodRequest;
import com.example.foodordering.dto.response.ApiResponse;
import com.example.foodordering.dto.response.FoodResponse;
import com.example.foodordering.entity.Food;
import com.example.foodordering.mapper.FoodResponseMapper;
import com.example.foodordering.repository.FoodRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service to handle guest-related operations, such as retrieving available dishes,
 * searching food items, filtering by category, and providing recommendations.
 */
@Service
public class GuestService {

    private final FoodRepository foodRepository;
    private final FoodResponseMapper foodResponseMapper;

    public GuestService(FoodRepository foodRepository, FoodResponseMapper foodResponseMapper) {
        this.foodRepository = foodRepository;
        this.foodResponseMapper = foodResponseMapper;
    }

    /**
     * Retrieves the list of available dishes (where isAvailable = true).
     *
     * @return ApiResponse containing available dishes or an error if none found.
     */
    public ApiResponse<List<FoodResponse>> getAvailableDishes() {
        List<FoodResponse> dishes = foodRepository.findByIsAvailableTrue()
                .stream()
                .map(foodResponseMapper::toFoodResponse)
                .toList();
        return dishes.isEmpty()
                ? ApiResponse.build(1404, "Failed", null)
                : ApiResponse.build(1000, "Success", dishes);
    }

    /**
     * Searches for food items by name (case-insensitive).
     *
     * @param request the search term to find matching food names
     * @return ApiResponse containing matched food items or an error if none found.
     */
    public ApiResponse<List<FoodResponse>> searchDishes(SearchFoodRequest request) {
        if (request.getKeyword() == null || request.getKeyword().trim().isEmpty()) {
            return ApiResponse.build(1404, "Failed", null);
        }
        List<FoodResponse> results = foodRepository.findByNameContainingIgnoreCase(request.getKeyword())
                .stream()
                .map(foodResponseMapper::toFoodResponse)
                .toList();
        return results.isEmpty()
                ? ApiResponse.build(1404, "Failed", null)
                : ApiResponse.build(1000, "Success", results);
    }


    /**
     * Retrieves a sorted list of available dishes based on sorting criteria.
     *
     * @param request sorting criteria (e.g., "price_asc", "price_desc")
     * @return ApiResponse containing sorted dishes.
     */
    public ApiResponse<List<FoodResponse>> getSortedDishes(SearchFoodRequest request) {
        String order = "price_asc".equals(request.getSortBy()) ? "ASC" : "DESC";
        List<FoodResponse> sortedDishes = foodRepository.findAvailableDishesSorted(order)
                .stream()
                .map(foodResponseMapper::toFoodResponse)
                .toList();
        return ApiResponse.build(1000, "Success", sortedDishes);
    }
    /**
     * Retrieves details of a specific food item.
     *
     * @param foodId the ID of the food item
     * @return ApiResponse containing food details or an error if not found.
     */
    public ApiResponse<FoodResponse> getFoodDetails(int foodId) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found"));
        return ApiResponse.build(1000, "Success", foodResponseMapper.toFoodResponse(food));
    }

//    /**
//     * Recommends food items similar to the search keyword.
//     *
//     * @param request the food item name to base recommendations on
//     * @return ApiResponse containing recommended dishes.
//     */
//    public ApiResponse<List<FoodResponse>> recommendDishes(SearchFoodRequest request) {
//        // Validate the keyword
//        if (request.getKeyword().trim().isEmpty()) {
//            return ApiResponse.build(1404, "Failed", null);
//        }
//
//        // Find a reference food item
//        Optional<Food> referenceFood = foodRepository.findFirstByNameContainingIgnoreCase(request.getKeyword());
//
//        if (referenceFood.isEmpty()) {
//            return ApiResponse.build(1404, "Failed", null);
//        }
//
//        BigDecimal minPrice = referenceFood.get().getPrice().multiply(new BigDecimal("0.8"));
//        BigDecimal maxPrice = referenceFood.get().getPrice().multiply(new BigDecimal("1.2"));
//
//        List<FoodResponse> recommendations = foodRepository
//                .findSimilarDishes(minPrice, maxPrice, referenceFood.get().getFoodId())
//                .stream()
//                .map(foodResponseMapper::toFoodResponse)
//                .toList();
//
//        return recommendations.isEmpty()
//                ? ApiResponse.build(1404, "Failed", null)
//                : ApiResponse.build(1000, "Success", recommendations);
//    }

}


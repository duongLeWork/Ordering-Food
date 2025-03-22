package com.example.foodordering.service;

import com.example.foodordering.dto.response.ApiResponse;
import com.example.foodordering.entity.Food;
import com.example.foodordering.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Service to handle guest-related operations, such as retrieving available dishes,
 * searching food items, filtering by category, and providing recommendations.
 */
@Service
public class GuestService {

    @Autowired
    private FoodRepository foodRepository;

    /**
     * Retrieves the list of available dishes (where isAvailable = true).
     *
     * @return ApiResponse containing available dishes or an error if none found.
     */
    public ApiResponse<List<Food>> getAvailableDishes() {
        List<Food> dishes = foodRepository.findByIsAvailableTrue();
        return dishes.isEmpty()
                ? ApiResponse.build(1404, "Failed", null)
                : ApiResponse.build(1000, "Success", dishes);
    }

    /**
     * Searches for food items by name (case-insensitive).
     *
     * @param keyword the search term to find matching food names
     * @return ApiResponse containing matched food items or an error if none found.
     */
    public ApiResponse<List<Food>> searchDishes(String keyword) {
        List<Food> results = foodRepository.findByNameContainingIgnoreCase(keyword);
        return results.isEmpty()
                ? ApiResponse.build(1404, "Không tìm thấy món ăn nào!", null)
                : ApiResponse.build(1000, "Success", results);
    }

    /**
     * Retrieves a sorted list of available dishes based on sorting criteria.
     *
     * @param sortBy sorting criteria (e.g., "price_asc", "price_desc")
     * @return ApiResponse containing sorted dishes.
     */
    public ApiResponse<List<Food>> getSortedDishes(String sortBy) {
        List<Food> dishes = foodRepository.findByIsAvailableTrue();

        switch (sortBy) {
            case "price_asc":
                dishes.sort(Comparator.comparing(Food::getPrice));
                break;
            case "price_desc":
                dishes.sort(Comparator.comparing(Food::getPrice).reversed());
                break;
            default:
                return ApiResponse.build(1400, "Invalid sort option!", null);
        }

        return ApiResponse.build(1000, "Success", dishes);
    }

    /**
     * Retrieves available dishes based on category.
     *
     * @param category the category name (e.g., "Pizza", "Drinks")
     * @return ApiResponse containing filtered dishes.
     */
    public ApiResponse<List<Food>> filterDishesByCategory(String category) {
        List<Food> filteredDishes = foodRepository.findByCategoryAndIsAvailableTrue(category);
        return filteredDishes.isEmpty()
                ? ApiResponse.build(1404, "Không có món ăn nào trong danh mục này!", null)
                : ApiResponse.build(1000, "Success", filteredDishes);
    }

    /**
     * Retrieves details of a specific food item.
     *
     * @param foodId the ID of the food item
     * @return ApiResponse containing food details or an error if not found.
     */
    public ApiResponse<Food> getFoodDetails(Integer foodId) {
        Optional<Food> food = foodRepository.findById(foodId);
        return food.map(value -> ApiResponse.build(1000, "Success", value))
                .orElseGet(() -> ApiResponse.build(1404, "Không tìm thấy món ăn!", null));
    }

    /**
     * Recommends food items similar to the search keyword.
     *
     * @param keyword the food item name to base recommendations on
     * @return ApiResponse containing recommended dishes.
     */
    public ApiResponse<List<Food>> recommendDishes(String keyword) {
        List<Food> recommendations = foodRepository.findSimilarDishes(keyword);
        return recommendations.isEmpty()
                ? ApiResponse.build(1404, "Không có gợi ý món ăn nào", null)
                : ApiResponse.build(1000, "Success", recommendations);
    }
}

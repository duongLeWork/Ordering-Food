package com.example.foodordering.controller;

import com.example.foodordering.dto.request.FoodRequest;
import com.example.foodordering.dto.response.ApiResponse;
import com.example.foodordering.dto.response.FoodResponse;
import com.example.foodordering.entity.Food;
import com.example.foodordering.service.GuestService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for guest-related operations such as retrieving available dishes,
 * searching for food, sorting, and filtering by category.
 */
@RestController
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    /**
     * Retrieves all available dishes.
     *
     * @return ApiResponse containing the list of available dishes.
     */
    @GetMapping("/dishes")
    public ApiResponse<List<FoodResponse>> getAvailableDishes() {
        return guestService.getAvailableDishes();
    }

    /**
     * Searches for dishes by keyword (case-insensitive).
     *
     * @param request the search term for food names.
     * @return ApiResponse containing matching dishes.
     */
    @PostMapping("/dishes/search")
    public ApiResponse<List<FoodResponse>> searchDishes(@RequestBody @Valid FoodRequest request) {
        return guestService.searchDishes(request);
    }
    /**
     * Retrieves a sorted list of available dishes based on sorting criteria.
     *
     * @param request sorting option (e.g., "price_asc", "price_desc").
     * @return ApiResponse containing sorted dishes.
     */
    @GetMapping("/dishes")
    public ApiResponse<List<FoodResponse>> getDishes(@RequestBody @Valid FoodRequest request) {
        return guestService.getSortedDishes(request);
    }

    /**
     * Retrieves details of a specific food item.
     *
     * @param foodId the ID of the food item.
     * @return ApiResponse containing food details.
     */
    @GetMapping("/dishes/{foodId}")
    public ApiResponse<FoodResponse> getFoodDetails(@PathVariable int foodId) {
        return guestService.getFoodDetails(foodId);
    }
    /**
     * Recommend similar dishes
     *
     * @param request the name of the dish.
     * @return ApiResponse list of similar foods.
     */
    @GetMapping("/dishes/recommendations")
    public ApiResponse<List<FoodResponse>> recommendDishes(@RequestBody @Valid FoodRequest request) {
        return guestService.recommendDishes(request);
    }
}

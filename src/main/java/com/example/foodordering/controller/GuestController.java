package com.example.foodordering.controller;

import com.example.foodordering.dto.response.ApiResponse;
import com.example.foodordering.entity.Food;
import com.example.foodordering.service.GuestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for guest-related operations such as retrieving available dishes,
 * searching for food, sorting, and filtering by category.
 */
@RestController
@RequestMapping("/guest")
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
    public ApiResponse<List<Food>> getAvailableDishes() {
        return guestService.getAvailableDishes();
    }

    /**
     * Searches for dishes by keyword (case-insensitive).
     *
     * @param keyword the search term for food names.
     * @return ApiResponse containing matching dishes.
     */
    @GetMapping("/search")
    public ApiResponse<List<Food>> searchDishes(@RequestParam String keyword) {
        return guestService.searchDishes(keyword);
    }

    /**
     * Retrieves a sorted list of available dishes based on sorting criteria.
     *
     * @param sortBy sorting option (e.g., "price_asc", "price_desc").
     * @return ApiResponse containing sorted dishes.
     */
    @GetMapping("/dishes/sorted")
    public ApiResponse<List<Food>> getSortedDishes(@RequestParam String sortBy) {
        return guestService.getSortedDishes(sortBy);
    }

    /**
     * Retrieves available dishes filtered by category.
     *
     * @param category the category of the food (e.g., "Pizza", "Drinks").
     * @return ApiResponse containing filtered dishes.
     */
    @GetMapping("/dishes/category")
    public ApiResponse<List<Food>> filterDishesByCategory(@RequestParam String category) {
        return guestService.filterDishesByCategory(category);
    }

    /**
     * Retrieves details of a specific food item.
     *
     * @param foodId the ID of the food item.
     * @return ApiResponse containing food details.
     */
    @GetMapping("/dishes/{foodId}")
    public ApiResponse<Food> getFoodDetails(@PathVariable Integer foodId) {
        return guestService.getFoodDetails(foodId);
    }

    /**
     * Recommend similar dishes
     *
     * @param keyword the name of the dish.
     * @return ApiResponse list of similar foods.
     */
    @GetMapping("/dishes/recommend")
    public ApiResponse<List<Food>> recommendDishes(@RequestParam String keyword) {
        return guestService.recommendDishes(keyword);
    }
}

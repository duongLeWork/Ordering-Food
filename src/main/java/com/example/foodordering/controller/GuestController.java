package com.example.foodordering.controller;

import com.example.foodordering.dto.request.SearchFoodRequest;
import com.example.foodordering.dto.response.ApiResponse;
import com.example.foodordering.dto.response.FoodResponse;
import com.example.foodordering.service.GuestService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for guest-related operations such as retrieving available dishes,
 * searching for food, sorting, and filtering by category.
 */
@RestController
@RequestMapping("/api") // Thêm @RequestMapping("/api") ở cấp class
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    /**
     * Retrieves all available dishes.
     * Danh sách món ăn (Dishes List): GET /api/dishes
     * @return ApiResponse containing the list of available dishes.
     */
    @GetMapping("/dishes")
    public ApiResponse<List<FoodResponse>> getAvailableDishes() {
        return guestService.getAvailableDishes();
    }

    /**
     * Searches for dishes by keyword (case-insensitive).
     * Tìm kiếm món ăn (Search Dishes): GET /api/dishes?keyword={keyword}
     * @param keyword, sortBy, category, maxResults
     * @return ApiResponse containing matching dishes.
     */
    @GetMapping("/dishes/search") // Đổi thành /dishes/search
    public ApiResponse<List<FoodResponse>> searchDishes(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer maxResults) {

        SearchFoodRequest request = new SearchFoodRequest();
        request.setKeyword(keyword);
        request.setSortBy(sortBy);
        request.setCategory(category);
        request.setMaxResults(maxResults);

        return guestService.searchDishes(request);
    }

    /**
     * Retrieves a sorted list of available dishes based on sorting criteria.
     *
     * @param request sorting option (e.g., "price_asc", "price_desc").
     * @return ApiResponse containing sorted dishes.
     */
    @GetMapping("/dishes/sort") // Đổi thành /dishes/sorted
    public ApiResponse<List<FoodResponse>> getDishes(@RequestBody @Valid SearchFoodRequest request) {
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
}
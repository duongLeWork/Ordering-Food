package com.example.foodordering.controller;

import com.example.foodordering.dto.request.SearchFoodRequest;
import com.example.foodordering.dto.response.FoodResponse;
import com.example.foodordering.service.GuestService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dishes")
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping
    public String getAvailableDishes(Model model) {
        List<FoodResponse> dishes = guestService.getAvailableDishes().getData();
        model.addAttribute("dishes", dishes);
        return "dishes/list"; // Thymeleaf template: dishes/list.html
    }

    @GetMapping("/search")
    public String searchDishes(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String sortBy,
                               @RequestParam(required = false) String category,
                               @RequestParam(required = false) Integer maxResults,
                               Model model) {
        SearchFoodRequest request = new SearchFoodRequest();
        request.setKeyword(keyword);
        request.setSortBy(sortBy);
        request.setCategory(category);
        request.setMaxResults(maxResults);

        List<FoodResponse> results = guestService.searchDishes(request).getData();
        model.addAttribute("dishes", results);
        return "dishes/search-results"; // Thymeleaf template: dishes/search-results.html
    }

    @GetMapping("/sort")
    public String getDishes(@ModelAttribute @Valid SearchFoodRequest request, Model model) {
        List<FoodResponse> sortedDishes = guestService.getSortedDishes(request).getData();
        model.addAttribute("dishes", sortedDishes);
        return "dishes/sorted-list"; // Thymeleaf template: dishes/sorted-list.html
    }

    @GetMapping("/{foodId}")
    public String getFoodDetails(@PathVariable int foodId, Model model) {
        FoodResponse food = guestService.getFoodDetails(foodId).getData();
        model.addAttribute("food", food);
        return "dishes/detail"; // Thymeleaf template: dishes/detail.html
    }
}

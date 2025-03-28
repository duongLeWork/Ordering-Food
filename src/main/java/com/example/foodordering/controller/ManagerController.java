package com.example.foodordering.controller;

import com.example.foodordering.dto.response.ApiResponse;
import com.example.foodordering.entity.Customer;
import com.example.foodordering.entity.Food;
import com.example.foodordering.entity.FoodOrder;
import com.example.foodordering.service.ManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    /**
     * Retrieves a list of all registered users.
     *
     * @return ResponseEntity containing a list of users.
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<Customer>>> getAllUsers() {
        ApiResponse<List<Customer>> response = managerService.getAllUsers();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Retrieves a list of all placed orders.
     *
     * @return ResponseEntity containing a list of orders.
     */
    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<FoodOrder>>> getAllOrders() {
        ApiResponse<List<FoodOrder>> response = managerService.getAllOrders();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Adds a new food item.
     *
     * @param food The Food entity to be added.
     * @return ResponseEntity containing the added food item.
     */
    @PostMapping("/food")
    public ResponseEntity<ApiResponse<Food>> addFood(@RequestBody Food food) {
        ApiResponse<Food> response = managerService.addFood(food);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Updates an existing food item.
     *
     * @param foodId      The ID of the food item to update.
     * @param updatedFood The Food entity containing updated information.
     * @return ResponseEntity containing the updated food item.
     */
    @PutMapping("/food/{foodId}")
    public ResponseEntity<ApiResponse<Food>> updateFood(@PathVariable int foodId, @RequestBody Food updatedFood) {
        ApiResponse<Food> response = managerService.updateFood(foodId, updatedFood);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Deletes a food item.
     *
     * @param foodId The ID of the food item to delete.
     * @return ResponseEntity indicating success.
     */
    @DeleteMapping("/food/{foodId}")
    public ResponseEntity<ApiResponse<String>> deleteFood(@PathVariable int foodId) {
        ApiResponse<String> response = managerService.deleteFood(foodId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Retrieves sales statistics.
     *
     * @return ResponseEntity containing sales statistics.
     */
    @GetMapping("/sales")
    public ResponseEntity<ApiResponse<List<FoodOrder>>> getSalesStatistics() {
        ApiResponse<List<FoodOrder>> response = managerService.getSalesStatistics();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
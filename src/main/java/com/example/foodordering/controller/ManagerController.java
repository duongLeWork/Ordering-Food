package com.example.foodordering.controller;

import com.example.foodordering.dto.request.FoodRequest;
import com.example.foodordering.dto.response.ApiResponse;
import com.example.foodordering.entity.Account;
import com.example.foodordering.entity.Food;
import com.example.foodordering.entity.FoodOrder;
import com.example.foodordering.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @GetMapping("/users/{role}")
    public ApiResponse<List<Account>> getUsersByRole(@PathVariable String role) {
        return managerService.getUsersByRole(role);
    }

    @GetMapping("/orders")
    public ApiResponse<List<FoodOrder>> getAllOrders() {
        return managerService.getAllOrders();
    }

    @PostMapping("/food")
    public ApiResponse<Food> addFood(@RequestBody FoodRequest foodRequest) {
        return managerService.addFood(foodRequest);
    }

    @PutMapping("/food/{foodId}")
    public ApiResponse<Food> updateFood(@PathVariable Integer foodId, @RequestBody FoodRequest foodRequest) {
        return managerService.updateFood(foodId, foodRequest);
    }

    @DeleteMapping("/food/{foodId}")
    public ApiResponse<String> deleteFood(@PathVariable Integer foodId) {
        return managerService.deleteFood(foodId);
    }

    @GetMapping("/sales")
    public ApiResponse<BigDecimal> getTotalSales() {
        return managerService.getTotalSales();
    }
}

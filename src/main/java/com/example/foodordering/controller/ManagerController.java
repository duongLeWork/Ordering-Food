package com.example.foodordering.controller;

import com.example.foodordering.dto.request.FoodRequest;
import com.example.foodordering.entity.Account;
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
    public List<Account> getUsersByRole(@PathVariable String role) {
        return managerService.getUsersByRole(role);
    }

    @GetMapping("/orders")
    public List<FoodOrder> getAllOrders() {
        return managerService.getAllOrders();
    }

    @PostMapping("/food")
    public String addFood(@RequestBody FoodRequest foodRequest) {
        return managerService.addFood(foodRequest);
    }

    @PutMapping("/food/{foodId}")
    public String updateFood(@PathVariable Integer foodId, @RequestBody FoodRequest foodRequest) {
        return managerService.updateFood(foodId, foodRequest);
    }

    @DeleteMapping("/food/{foodId}")
    public String deleteFood(@PathVariable Integer foodId) {
        return managerService.deleteFood(foodId);
    }

    @GetMapping("/sales")
    public BigDecimal getTotalSales() {
        return managerService.getTotalSales();
    }
}

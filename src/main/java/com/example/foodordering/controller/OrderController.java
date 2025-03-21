package com.example.foodordering.controller;

import com.example.foodordering.dto.request.OrderRequest;
import com.example.foodordering.dto.request.UpdateOrderStatusRequest;
import com.example.foodordering.entity.FoodOrder;
import com.example.foodordering.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public String createOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @PutMapping("/update-status")
    public String updateOrderStatus(@RequestBody UpdateOrderStatusRequest request) {
        return orderService.updateOrderStatus(request);
    }

    @GetMapping("/customer/{customerId}")
    public List<FoodOrder> getOrdersByCustomer(@PathVariable Integer customerId) {
        return orderService.getOrdersByCustomer(customerId);
    }

    @GetMapping("/details/{orderId}")
    public Optional<FoodOrder> getOrderDetails(@PathVariable Integer orderId) {
        return orderService.getOrderDetails(orderId);
    }
}

package com.example.foodordering.controller;

import com.example.foodordering.dto.request.OrderRequest;
import com.example.foodordering.dto.response.ApiResponse;
import com.example.foodordering.entity.Customer;
import com.example.foodordering.entity.FoodOrder;
import com.example.foodordering.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PutMapping("/update-profile/{customerId}")
    public ApiResponse<Customer> updateProfile(@PathVariable Integer customerId, @RequestBody Customer updatedCustomer) {
        return customerService.updateProfile(customerId, updatedCustomer);
    }

    @PostMapping("/place-order")
    public ApiResponse<FoodOrder> placeOrder(@RequestBody OrderRequest orderRequest) {
        return customerService.placeOrder(orderRequest);
    }

    @GetMapping("/track-order/{orderId}")
    public ApiResponse<String> trackOrder(@PathVariable Integer orderId) {
        return customerService.trackOrder(orderId);
    }

    @GetMapping("/order-details/{orderId}")
    public ApiResponse<FoodOrder> getOrderDetails(@PathVariable Integer orderId) {
        return customerService.getOrderDetails(orderId);
    }
}

package com.example.foodordering.controller;

import com.example.foodordering.dto.response.ApiResponse;
import com.example.foodordering.entity.FoodOrder;
import com.example.foodordering.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Creates a new order for a specific customer.
     *
     * @param customerId ID of the customer placing the order.
     * @return ResponseEntity containing the newly created order.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<FoodOrder>> createOrder(@RequestParam int customerId) {
        ApiResponse<FoodOrder> response = orderService.createOrder(customerId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Retrieves a list of orders for a specific customer.
     *
     * @param customerId ID of the customer.
     * @return ResponseEntity containing the list of orders.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<FoodOrder>>> getOrderList(@RequestParam int customerId) {
        ApiResponse<List<FoodOrder>> response = orderService.getOrderList(customerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Retrieves details of a specific order for a customer.
     *
     * @param orderId    ID of the order.
     * @param customerId ID of the customer.
     * @return ResponseEntity containing the order details.
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<FoodOrder>> getOrderDetails(@PathVariable int orderId, @RequestParam int customerId) {
        ApiResponse<FoodOrder> response = orderService.getOrderDetails(orderId, customerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
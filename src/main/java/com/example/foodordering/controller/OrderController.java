package com.example.foodordering.controller;

import com.example.foodordering.entity.FoodOrder;
import com.example.foodordering.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing customer orders.
 * Supports creating, listing, and retrieving order details.
 */
@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Creates a new order for a specific customer.
     *
     * @param customerId ID of the customer placing the order.
     * @param model      Model to add order data.
     * @return Thymeleaf template for order success page.
     */
    @PostMapping
    public String createOrder(@RequestParam int customerId, Model model) {
        FoodOrder order = orderService.createOrder().getData();
        model.addAttribute("order", order);
        return "orders/success"; // Thymeleaf template: orders/order-success.html
    }

    /**
     * Retrieves a list of orders for a specific customer.
     *
     * @param customerId ID of the customer.
     * @param model      Model to add order list data.
     * @return Thymeleaf template for order list page.
     */
    @GetMapping
    public String getOrderList(@RequestParam int customerId, Model model) {
        List<FoodOrder> orders = orderService.getOrderList().getData();
        model.addAttribute("orders", orders);
        return "orders/list"; // Thymeleaf template: orders/order-list.html
    }

    /**
     * Retrieves details of a specific order for a customer.
     *
     * @param orderId    ID of the order.
     * @param customerId ID of the customer.
     * @param model      Model to add order detail data.
     * @return Thymeleaf template for order detail page.
     */
    @GetMapping("/{orderId}")
    public String getOrderDetails(@PathVariable int orderId, @RequestParam int customerId, Model model) {
        FoodOrder order = orderService.getOrderDetails(orderId).getData();
        model.addAttribute("order", order);
        return "orders/detail"; // Thymeleaf template: orders/order-details.html
    }
}

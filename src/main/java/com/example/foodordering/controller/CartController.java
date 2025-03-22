package com.example.foodordering.controller;

import com.example.foodordering.dto.request.CartItemRequest;
import com.example.foodordering.dto.response.ApiResponse;
import com.example.foodordering.entity.OrderMenuItem;
import com.example.foodordering.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{customerId}")
    public ApiResponse<List<OrderMenuItem>> getCart(@PathVariable Integer customerId) {
        return cartService.getCart(customerId);
    }

    @PostMapping("/add")
    public ApiResponse<OrderMenuItem> addItemToCart(@RequestBody CartItemRequest request) {
        return cartService.addItemToCart(request);
    }

    @PutMapping("/update/{cartItemId}")
    public ApiResponse<OrderMenuItem> updateItemQuantity(@PathVariable Integer cartItemId, @RequestParam Integer newQuantity) {
        return cartService.updateItemQuantity(cartItemId, newQuantity);
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ApiResponse<String> removeItemFromCart(@PathVariable Integer cartItemId) {
        return cartService.removeItemFromCart(cartItemId);
    }

    @DeleteMapping("/clear/{customerId}")
    public ApiResponse<String> clearCart(@PathVariable Integer customerId) {
        return cartService.clearCart(customerId);
    }
}

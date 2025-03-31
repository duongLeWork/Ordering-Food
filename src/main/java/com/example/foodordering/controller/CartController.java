package com.example.foodordering.controller;

import com.example.foodordering.dto.request.CartItemRequest;
import com.example.foodordering.entity.OrderMenuItem;
import com.example.foodordering.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing cart operations using Thymeleaf.
 * Supports retrieving cart items, adding, updating, and removing items.
 */
@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Retrieves the cart items of a specific customer.
     * @param model      Model to add cart items.
     * @return Thymeleaf template for the cart page.
     */
    @GetMapping("/")
    public String getCart(Model model) {
        List<OrderMenuItem> cartItems = cartService.getCart().getData();
        model.addAttribute("cartItems", cartItems);
        return "cart/cart-list"; // Thymeleaf template: cart/cart-list.html
    }

    /**
     * Adds an item to the cart.
     *
     * @param request CartItemRequest containing food item and quantity.
     * @param model   Model to add the updated cart item.
     * @return Thymeleaf template for cart item added page.
     */
    @PostMapping("/items")
    public String addItemToCart(@ModelAttribute CartItemRequest request, Model model) {
        OrderMenuItem item = cartService.addItemToCart(request).getData();
        model.addAttribute("item", item);
        return "cart/cart-item"; // Thymeleaf template: cart/cart-item.html
    }

    /**
     * Updates the quantity of a cart item.
     *
     * @param cartItemId  ID of the cart item.
     * @param newQuantity New quantity to update.
     * @param model       Model to add updated cart item.
     * @return Thymeleaf template for updated cart item.
     */
    @PutMapping("/items/{cartItemId}")
    public String updateItemQuantity(@PathVariable int cartItemId, @RequestParam int newQuantity, Model model) {
        OrderMenuItem item = cartService.updateItemQuantity(cartItemId, newQuantity).getData();
        model.addAttribute("item", item);
        return "cart/cart-item"; // Thymeleaf template: cart/cart-item.html
    }

    /**
     * Removes an item from the cart.
     *
     * @param cartItemId ID of the cart item to remove.
     * @return Redirect to cart page after removal.
     */
    @DeleteMapping("/items/{cartItemId}")
    public String removeItemFromCart(@PathVariable int cartItemId) {
        cartService.removeItemFromCart(cartItemId);
        return "redirect:/cart"; // Redirect to cart page
    }

    /**
     * Clears the entire cart of a customer.
     *
     * @return Redirect to cart page after clearing.
     */
    @DeleteMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart"; // Redirect to cart page
    }
}

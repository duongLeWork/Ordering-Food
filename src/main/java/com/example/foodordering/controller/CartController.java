package com.example.foodordering.controller;

import com.example.foodordering.config.CustomUserDetails;
import com.example.foodordering.dto.request.CartItemRequest;
import com.example.foodordering.entity.OrderMenuItem;
import com.example.foodordering.service.CartService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            int customerId = (int) userDetails.getAccountId();
            List<OrderMenuItem> cartItems = cartService.getCart(customerId).getData();
            model.addAttribute("cartItems", cartItems);
            return "cart/list";
        }
        return "redirect:/login"; // Hoặc xử lý trường hợp chưa đăng nhập khác
    }

    /**
     * Adds an item to the cart.
     *
     * @param request CartItemRequest containing food item and quantity.
     * @return Redirect to cart page after adding.
     */
    @PostMapping("/items")
    public String addItemToCart(@ModelAttribute CartItemRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            int customerId = (int) userDetails.getAccountId();
            request.setCustomerId(customerId); // Set customerId từ thông tin đăng nhập
            cartService.addItemToCart(request);
            return "redirect:/cart/"; // Chuyển hướng đến trang giỏ hàng sau khi thêm
        }
        return "redirect:/login"; // Hoặc xử lý trường hợp chưa đăng nhập khác
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            int customerId = (int) userDetails.getAccountId();
            OrderMenuItem item = cartService.updateItemQuantity(customerId, cartItemId, newQuantity).getData();
            model.addAttribute("item", item);
            return "cart/item";
        }
        return "redirect:/login"; // Hoặc xử lý trường hợp chưa đăng nhập khác
    }

    /**
     * Removes an item from the cart.
     *
     * @param cartItemId ID of the cart item to remove.
     * @return Redirect to cart page after removal.
     */
    @DeleteMapping("/items/{cartItemId}")
    public String removeItemFromCart(@PathVariable int cartItemId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            int customerId = (int) userDetails.getAccountId();
            cartService.removeItemFromCart(customerId, cartItemId);
            return "redirect:/cart/"; // Redirect to cart page
        }
        return "redirect:/login"; // Hoặc xử lý trường hợp chưa đăng nhập khác
    }

    /**
     * Clears the entire cart of a customer.
     *
     * @return Redirect to cart page after clearing.
     */
    @DeleteMapping("/clear")
    public String clearCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            int customerId = (int) userDetails.getAccountId();
            cartService.clearCart(customerId);
            return "redirect:/cart/"; // Redirect to cart page
        }
        return "redirect:/login"; // Hoặc xử lý trường hợp chưa đăng nhập khác
    }
}
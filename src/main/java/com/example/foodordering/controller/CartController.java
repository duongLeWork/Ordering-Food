package com.example.foodordering.controller;

import com.example.foodordering.dto.request.CartItemRequest;
import com.example.foodordering.dto.response.ApiResponse;
import com.example.foodordering.entity.OrderMenuItem;
import com.example.foodordering.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller để quản lý các thao tác liên quan đến giỏ hàng.
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Lấy danh sách các món trong giỏ hàng của một khách hàng.
     *
     * @param customerId ID của khách hàng.
     * @return ResponseEntity chứa danh sách các mục trong giỏ hàng hoặc thông báo lỗi nếu giỏ hàng trống.
     */
    @GetMapping("/{customerId}") 
    public ResponseEntity<ApiResponse<List<OrderMenuItem>>> getCart(@PathVariable int customerId) {
        ApiResponse<List<OrderMenuItem>> response = cartService.getCart(customerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Thêm một món ăn vào giỏ hàng của người dùng.
     *
     * @param request Đối tượng CartItemRequest chứa thông tin món ăn và số lượng.
     * @return ResponseEntity chứa thông tin của món ăn vừa được thêm vào giỏ hàng.
     */
    @PostMapping("/items")
    public ResponseEntity<ApiResponse<OrderMenuItem>> addItemToCart(@RequestBody CartItemRequest request) {
        ApiResponse<OrderMenuItem> response = cartService.addItemToCart(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Cập nhật số lượng của một món trong giỏ hàng.
     *
     * @param cartItemId ID của món ăn trong giỏ hàng.
     * @param newQuantity Số lượng mới cần cập nhật.
     * @return ResponseEntity chứa thông tin cập nhật của món ăn.
     */
    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<OrderMenuItem>> updateItemQuantity(@PathVariable int cartItemId, @RequestParam int newQuantity) {
        ApiResponse<OrderMenuItem> response = cartService.updateItemQuantity(cartItemId, newQuantity);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Xóa một món ăn khỏi giỏ hàng.
     *
     * @param cartItemId ID của món ăn cần xóa.
     * @return ResponseEntity chứa thông báo xác nhận việc xóa thành công hoặc thất bại.
     */
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<String>> removeItemFromCart(@PathVariable int cartItemId) {
        ApiResponse<String> response = cartService.removeItemFromCart(cartItemId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Xóa toàn bộ giỏ hàng của một khách hàng.
     *
     * @param customerId ID của khách hàng có giỏ hàng cần được xóa.
     * @return ResponseEntity chứa thông báo xác nhận việc xóa giỏ hàng thành công.
     */
    @DeleteMapping("/{customerId}") 
    public ResponseEntity<ApiResponse<String>> clearCart(@PathVariable int customerId) {
        ApiResponse<String> response = cartService.clearCart(customerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
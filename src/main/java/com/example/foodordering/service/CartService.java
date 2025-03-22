package com.example.foodordering.service;

import com.example.foodordering.dto.request.CartItemRequest;
import com.example.foodordering.dto.response.ApiResponse;
import com.example.foodordering.entity.Customer;
import com.example.foodordering.entity.Food;
import com.example.foodordering.entity.OrderMenuItem;
import com.example.foodordering.repository.CustomerRepository;
import com.example.foodordering.repository.FoodRepository;
import com.example.foodordering.repository.OrderMenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private OrderMenuItemRepository orderMenuItemRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Lấy giỏ hàng của khách hàng
     */
    public ApiResponse<List<OrderMenuItem>> getCart(Integer customerId) {
        List<OrderMenuItem> cartItems = orderMenuItemRepository.findByFoodOrder_Customer_CustomerId(customerId);

        ApiResponse<List<OrderMenuItem>> response = new ApiResponse<>();
        response.setCode(1000);
        response.setMessage("Giỏ hàng của khách hàng");
        response.setData(cartItems);

        return response;
    }

    /**
     * Thêm món vào giỏ hàng
     */
    public ApiResponse<OrderMenuItem> addItemToCart(CartItemRequest request) {
        ApiResponse<OrderMenuItem> response = new ApiResponse<>();

        Optional<Customer> customerOpt = customerRepository.findById(request.getCustomerId());
        if (customerOpt.isEmpty()) {
            response.setCode(1404);
            response.setMessage("Khách hàng không tồn tại!");
            response.setData(null);
            return response;
        }

        Optional<Food> foodOpt = foodRepository.findById(request.getFoodId());
        if (foodOpt.isEmpty()) {
            response.setCode(1404);
            response.setMessage("Món ăn không tồn tại!");
            response.setData(null);
            return response;
        }

        OrderMenuItem cartItem = new OrderMenuItem();
        cartItem.setFood(foodOpt.get());
        cartItem.setQuantityOrdered(request.getQuantity());

        orderMenuItemRepository.save(cartItem);

        response.setCode(1201);
        response.setMessage("Đã thêm món vào giỏ hàng!");
        response.setData(cartItem);
        return response;
    }

    /**
     * Cập nhật số lượng món ăn trong giỏ hàng
     */
    public ApiResponse<OrderMenuItem> updateItemQuantity(Integer cartItemId, Integer newQuantity) {
        ApiResponse<OrderMenuItem> response = new ApiResponse<>();

        Optional<OrderMenuItem> cartItemOpt = orderMenuItemRepository.findById(cartItemId);
        if (cartItemOpt.isEmpty()) {
            response.setCode(1404);
            response.setMessage("Không tìm thấy món trong giỏ hàng!");
            response.setData(null);
            return response;
        }

        OrderMenuItem cartItem = cartItemOpt.get();
        cartItem.setQuantityOrdered(newQuantity);
        orderMenuItemRepository.save(cartItem);

        response.setCode(1000);
        response.setMessage("Cập nhật số lượng thành công!");
        response.setData(cartItem);
        return response;
    }

    /**
     * Xóa món khỏi giỏ hàng
     */
    public ApiResponse<String> removeItemFromCart(Integer cartItemId) {
        ApiResponse<String> response = new ApiResponse<>();

        if (!orderMenuItemRepository.existsById(cartItemId)) {
            response.setCode(1404);
            response.setMessage("Không tìm thấy món ăn trong giỏ hàng!");
            response.setData(null);
            return response;
        }

        orderMenuItemRepository.deleteById(cartItemId);

        response.setCode(1000);
        response.setMessage("Đã xóa món khỏi giỏ hàng!");
        response.setData("Success");
        return response;
    }

    /**
     * Xóa toàn bộ giỏ hàng của khách hàng
     */
    public ApiResponse<String> clearCart(Integer customerId) {
        orderMenuItemRepository.deleteByFoodOrder_Customer_CustomerId(customerId);

        ApiResponse<String> response = new ApiResponse<>();
        response.setCode(1000);
        response.setMessage("Đã xóa toàn bộ giỏ hàng!");
        response.setData("Success");

        return response;
    }

}

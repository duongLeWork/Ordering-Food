package com.example.foodordering.service;

import com.example.foodordering.dto.request.CartItemRequest;
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
    public List<OrderMenuItem> getCart(Integer customerId) {
        return orderMenuItemRepository.findByCustomerCustomerId(customerId);
    }

    /**
     * Thêm món vào giỏ hàng
     */
    public String addItemToCart(CartItemRequest request) {
        Optional<Customer> customerOpt = customerRepository.findById(request.getCustomerId());
        Optional<Food> foodOpt = foodRepository.findById(request.getFoodId());

        if (customerOpt.isEmpty()) return "Khách hàng không tồn tại!";
        if (foodOpt.isEmpty()) return "Món ăn không tồn tại!";

        OrderMenuItem cartItem = new OrderMenuItem();
        cartItem.setFood(foodOpt.get());
        cartItem.setQuantityOrdered(request.getQuantity());

        orderMenuItemRepository.save(cartItem);
        return "Đã thêm món vào giỏ hàng!";
    }

    /**
     * Cập nhật số lượng món ăn trong giỏ hàng
     */
    public String updateItemQuantity(Integer cartItemId, Integer newQuantity) {
        Optional<OrderMenuItem> cartItemOpt = orderMenuItemRepository.findById(cartItemId);
        if (cartItemOpt.isEmpty()) return "Không tìm thấy món trong giỏ hàng!";

        OrderMenuItem cartItem = cartItemOpt.get();
        cartItem.setQuantityOrdered(newQuantity);
        orderMenuItemRepository.save(cartItem);
        return "Cập nhật số lượng thành công!";
    }

    /**
     * Xóa món khỏi giỏ hàng
     */
    public String removeItemFromCart(Integer cartItemId) {
        if (!orderMenuItemRepository.existsById(cartItemId)) return "Không tìm thấy món ăn trong giỏ hàng!";
        orderMenuItemRepository.deleteById(cartItemId);
        return "Đã xóa món khỏi giỏ hàng!";
    }

    /**
     * Xóa toàn bộ giỏ hàng của khách hàng
     */
    public String clearCart(Integer customerId) {
        orderMenuItemRepository.deleteByCustomerCustomerId(customerId);
        return "Đã xóa toàn bộ giỏ hàng!";
    }
}

package com.example.foodordering.service;

import com.example.foodordering.dto.request.FoodRequest;
import com.example.foodordering.entity.Account;
import com.example.foodordering.entity.Food;
import com.example.foodordering.entity.FoodOrder;
import com.example.foodordering.repository.AccountRepository;
import com.example.foodordering.repository.FoodOrderRepository;
import com.example.foodordering.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ManagerService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FoodOrderRepository foodOrderRepository;

    @Autowired
    private FoodRepository foodRepository;

    /**
     * Lấy danh sách tài khoản theo vai trò (CUSTOMER hoặc MANAGER)
     * TODO: Em chưa thấy cái này cần cho lắm nên chưa thêm, đặt chỗ trước
     */
    public List<Account> getUsersByRole(String role) {
        return accountRepository.findByRole(role);
    }

    /**
     * Lấy danh sách đơn hàng
     */
    public List<FoodOrder> getAllOrders() {
        return foodOrderRepository.findAll();
    }

    /**
     * Thêm món ăn vào thực đơn
     */
    public String addFood(FoodRequest foodRequest) {
        Food food = new Food();
        food.setName(foodRequest.getName());
        food.setPrice(foodRequest.getPrice());
        food.setDescription(foodRequest.getDescription());
        food.setImage(foodRequest.getImage());
        food.setIsAvailable(foodRequest.getIsAvailable());

        foodRepository.save(food);
        return "Món ăn đã được thêm thành công!";
    }

    /**
     * Cập nhật thông tin món ăn
     */
    public String updateFood(Integer foodId, FoodRequest foodRequest) {
        Optional<Food> foodOpt = foodRepository.findById(foodId);
        if (foodOpt.isEmpty()) {
            return "Không tìm thấy món ăn!";
        }

        Food food = foodOpt.get();
        food.setName(foodRequest.getName());
        food.setPrice(foodRequest.getPrice());
        food.setDescription(foodRequest.getDescription());
        food.setImage(foodRequest.getImage());
        food.setIsAvailable(foodRequest.getIsAvailable());

        foodRepository.save(food);
        return "Cập nhật món ăn thành công!";
    }

    /**
     * Xóa món ăn
     */
    public String deleteFood(Integer foodId) {
        if (!foodRepository.existsById(foodId)) {
            return "Không tìm thấy món ăn!";
        }
        foodRepository.deleteById(foodId);
        return "Món ăn đã bị xóa!";
    }

    /**
     * Xem tổng doanh thu từ đơn hàng
     */
    public BigDecimal getTotalSales() {
        return foodOrderRepository.calculateTotalSales();
    }

}

package com.example.foodordering.service;

import com.example.foodordering.dto.request.FoodRequest;
import com.example.foodordering.dto.response.ApiResponse;
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
     * Lấy danh sách tài khoản theo vai trò
     */
    public ApiResponse<List<Account>> getUsersByRole(String role) {
        List<Account> users = accountRepository.findByRole(role);

        ApiResponse<List<Account>> response = new ApiResponse<>();
        response.setCode(1000);
        response.setMessage("Danh sách người dùng theo vai trò");
        response.setData(users);

        return response;
    }

    /**
     * Lấy danh sách tất cả đơn hàng
     */
    public ApiResponse<List<FoodOrder>> getAllOrders() {
        List<FoodOrder> orders = foodOrderRepository.findAll();

        ApiResponse<List<FoodOrder>> response = new ApiResponse<>();
        response.setCode(1000);
        response.setMessage("Danh sách tất cả đơn hàng");
        response.setData(orders);

        return response;
    }

    /**
     * Thêm món ăn vào thực đơn
     */
    public ApiResponse<Food> addFood(FoodRequest foodRequest) {
        Food food = new Food();
        food.setName(foodRequest.getName());
        food.setPrice(foodRequest.getPrice());
        food.setDescription(foodRequest.getDescription());
        food.setImage(foodRequest.getImage());
        food.setIsAvailable(foodRequest.getIsAvailable());

        foodRepository.save(food);

        ApiResponse<Food> response = new ApiResponse<>();
        response.setCode(1201);
        response.setMessage("Món ăn đã được thêm thành công!");
        response.setData(food);

        return response;
    }

    /**
     * Cập nhật thông tin món ăn
     */
    public ApiResponse<Food> updateFood(int foodId, FoodRequest foodRequest) {
        ApiResponse<Food> response = new ApiResponse<>();
        Optional<Food> foodOpt = foodRepository.findById(foodId);
        if (foodOpt.isEmpty()) {
            response.setCode(1404);
            response.setMessage("Không tìm thấy món ăn!");
            response.setData(null);
            return response;
        }

        Food food = foodOpt.get();
        food.setName(foodRequest.getName());
        food.setPrice(foodRequest.getPrice());
        food.setDescription(foodRequest.getDescription());
        food.setImage(foodRequest.getImage());
        food.setIsAvailable(foodRequest.getIsAvailable());

        foodRepository.save(food);

        response.setCode(1000);
        response.setMessage("Cập nhật món ăn thành công!");
        response.setData(food);
        return response;
    }

    /**
     * Xóa món ăn
     */
    public ApiResponse<String> deleteFood(int foodId) {
        ApiResponse<String> response = new ApiResponse<>();

        if (!foodRepository.existsById(foodId)) {
            response.setCode(1404);
            response.setMessage("Không tìm thấy món ăn!");
            response.setData(null);
            return response;
        }

        foodRepository.deleteById(foodId);

        response.setCode(1000);
        response.setMessage("Món ăn đã bị xóa!");
        response.setData("Success");
        return response;
    }

    /**
     * Xem tổng doanh thu từ đơn hàng
     */
    public ApiResponse<BigDecimal> getTotalSales() {
        BigDecimal totalSales = foodOrderRepository.calculateTotalSales();

        ApiResponse<BigDecimal> response = new ApiResponse<>();
        response.setCode(1000);
        response.setMessage("Tổng doanh thu từ đơn hàng");
        response.setData(totalSales);

        return response;
    }
}

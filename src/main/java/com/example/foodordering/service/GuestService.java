package com.example.foodordering.service;

import com.example.foodordering.dto.request.AccountCreationRequest;
import com.example.foodordering.dto.response.ApiResponse;
import com.example.foodordering.entity.Account;
import com.example.foodordering.entity.Food;
import com.example.foodordering.repository.AccountRepository;
import com.example.foodordering.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Lấy danh sách món ăn có sẵn
     */
    public ApiResponse<List<Food>> getAvailableDishes() {
        List<Food> dishes = foodRepository.findByIsAvailableTrue();

        ApiResponse<List<Food>> response = new ApiResponse<>();
        response.setCode(1000);
        response.setMessage("Success");
        response.setData(dishes);

        return response;
    }

    /**
     * Tìm kiếm món ăn theo từ khóa (không phân biệt hoa thường)
     */
    public ApiResponse<List<Food>> searchDishes(String keyword) {
        List<Food> results = foodRepository.findByNameContainingIgnoreCase(keyword);

        ApiResponse<List<Food>> response = new ApiResponse<>();
        if (results.isEmpty()) {
            response.setCode(1404);
            response.setMessage("Không tìm thấy món ăn nào!");
            response.setData(null);
        } else {
            response.setCode(1000);
            response.setMessage("Success");
            response.setData(results);
        }

        return response;
    }

    /**
     * Đăng ký tài khoản mới
     */
    public ApiResponse<Account> createAccount(AccountCreationRequest accountRequest) {
        ApiResponse<Account> response = new ApiResponse<>();

        if (accountRepository.existsByUsername(accountRequest.getUsername())) {
            response.setCode(1400);
            response.setMessage("Tên người dùng đã tồn tại!");
            response.setData(null);
            return response;
        }

        Account newAccount = new Account();
        newAccount.setUsername(accountRequest.getUsername());
        newAccount.setPassword(accountRequest.getPassword()); // Không mã hóa mật khẩu
        newAccount.setEmail(accountRequest.getEmail());
        newAccount.setRole(accountRequest.getRole());

        accountRepository.save(newAccount);

        response.setCode(1201);
        response.setMessage("Tài khoản đã được tạo thành công!");
        response.setData(newAccount);

        return response;
    }
}

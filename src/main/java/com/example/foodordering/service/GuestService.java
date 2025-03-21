package com.example.foodordering.service;

import com.example.foodordering.dto.request.AccountCreationRequest;
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

    public List<Food> getAvailableDishes() {
        return foodRepository.findByIsAvailableTrue();
    }

    public List<Food> searchDishes(String keyword) {
        return foodRepository.findByNameContainingIgnoreCase(keyword);
    }

    public String createAccount(AccountCreationRequest accountRequest) {
        if (accountRepository.existsByUsername(accountRequest.getUsername())) {
            return "Tên người dùng đã tồn tại!";
        }

        Account newAccount = new Account();
        newAccount.setUsername(accountRequest.getUsername());
        newAccount.setPassword(accountRequest.getPassword());
        newAccount.setEmail(accountRequest.getEmail());
        newAccount.setRole(accountRequest.getRole());
        accountRepository.save(newAccount);
        return "Tài khoản đã được tạo thành công!";
    }
}

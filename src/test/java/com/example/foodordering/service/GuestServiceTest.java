package com.example.foodordering.service;

import com.example.foodordering.dto.request.AccountCreationRequest;
import com.example.foodordering.entity.Account;
import com.example.foodordering.entity.Food;
import com.example.foodordering.repository.AccountRepository;
import com.example.foodordering.repository.FoodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GuestServiceTest {

    @InjectMocks
    private GuestService guestService;

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAvailableDishes() {
        // Mock danh sách món ăn có sẵn
        Food food1 = new Food();
        food1.setFoodId(1);
        food1.setName("Pizza");
        food1.setIsAvailable(true);

        Food food2 = new Food();
        food2.setFoodId(2);
        food2.setName("Burger");
        food2.setIsAvailable(true);

        when(foodRepository.findByIsAvailableTrue()).thenReturn(Arrays.asList(food1, food2));

        // Gọi service
        List<Food> availableDishes = guestService.getAvailableDishes();

        // Kiểm tra kết quả
        assertEquals(2, availableDishes.size());
        assertEquals("Pizza", availableDishes.get(0).getName());
        assertEquals("Burger", availableDishes.get(1).getName());

        // Kiểm tra repository có được gọi đúng không
        verify(foodRepository, times(1)).findByIsAvailableTrue();
    }

    @Test
    void testSearchDishes() {
        // Mock kết quả tìm kiếm món ăn
        Food food = new Food();
        food.setFoodId(1);
        food.setName("Pizza");

        when(foodRepository.findByNameContainingIgnoreCase("pizza")).thenReturn(Arrays.asList(food));

        // Gọi service
        List<Food> searchResults = guestService.searchDishes("pizza");

        // Kiểm tra kết quả
        assertEquals(1, searchResults.size());
        assertEquals("Pizza", searchResults.get(0).getName());

        // Kiểm tra repository có được gọi đúng không
        verify(foodRepository, times(1)).findByNameContainingIgnoreCase("pizza");
    }

    @Test
    void testCreateAccount_Success() {
        AccountCreationRequest accountRequest = new AccountCreationRequest();
        accountRequest.setUsername("testuser");
        accountRequest.setPassword("password123"); // Không cần encode
        accountRequest.setEmail("test@example.com");
        accountRequest.setRole("CUSTOMER");

        when(accountRepository.existsByUsername("testuser")).thenReturn(false);

        String result = guestService.createAccount(accountRequest);

        assertEquals("Tài khoản đã được tạo thành công!", result);
        verify(accountRepository, times(1)).save(any(Account.class));
    }


    @Test
    void testCreateAccount_Fail_UsernameExists() {
        AccountCreationRequest accountRequest = new AccountCreationRequest();
        accountRequest.setUsername("existingUser");
        accountRequest.setPassword("password123");
        accountRequest.setEmail("test@example.com");
        accountRequest.setRole("CUSTOMER");

        when(accountRepository.existsByUsername("existingUser")).thenReturn(true);

        String result = guestService.createAccount(accountRequest);

        assertEquals("Tên người dùng đã tồn tại!", result);
        verify(accountRepository, never()).save(any(Account.class));
    }

}

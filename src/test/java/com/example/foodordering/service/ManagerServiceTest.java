package com.example.foodordering.service;

import com.example.foodordering.dto.request.FoodRequest;
import com.example.foodordering.entity.Account;
import com.example.foodordering.entity.Food;
import com.example.foodordering.repository.AccountRepository;
import com.example.foodordering.repository.FoodOrderRepository;
import com.example.foodordering.repository.FoodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ManagerServiceTest {

    @InjectMocks
    private ManagerService managerService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private FoodOrderRepository foodOrderRepository;

    @Mock
    private FoodRepository foodRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsersByRole() {
        Account account = new Account();
        account.setUsername("testManager");
        account.setRole("MANAGER");
        when(accountRepository.findByRole("MANAGER")).thenReturn(List.of(account));
        List<Account> managers = managerService.getUsersByRole("MANAGER");
        assertEquals(1, managers.size());
        assertEquals("testManager", managers.get(0).getUsername());
    }

    @Test
    void testAddFood() {
        FoodRequest foodRequest = new FoodRequest();
        foodRequest.setName("Pasta");
        foodRequest.setPrice(new BigDecimal("12.50"));
        foodRequest.setDescription("Delicious pasta");
        foodRequest.setImage("pasta.jpg");
        foodRequest.setIsAvailable(true);

        String result = managerService.addFood(foodRequest);

        assertEquals("Món ăn đã được thêm thành công!", result);
        verify(foodRepository, times(1)).save(any(Food.class));
    }

    @Test
    void testUpdateFood_Success() {
        Food existingFood = new Food();
        existingFood.setFoodId(1);
        existingFood.setName("Burger");

        FoodRequest foodRequest = new FoodRequest();
        foodRequest.setName("Updated Burger");
        foodRequest.setPrice(new BigDecimal("10.99"));
        foodRequest.setDescription("Updated description");
        foodRequest.setImage("updated.jpg");
        foodRequest.setIsAvailable(false);

        when(foodRepository.findById(1)).thenReturn(Optional.of(existingFood));

        String result = managerService.updateFood(1, foodRequest);

        assertEquals("Cập nhật món ăn thành công!", result);
        verify(foodRepository, times(1)).save(existingFood);
    }

    @Test
    void testDeleteFood_Success() {
        when(foodRepository.existsById(1)).thenReturn(true);

        String result = managerService.deleteFood(1);

        assertEquals("Món ăn đã bị xóa!", result);
        verify(foodRepository, times(1)).deleteById(1);
    }
}

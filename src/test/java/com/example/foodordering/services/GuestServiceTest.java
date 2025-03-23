package com.example.foodordering.services;

import com.example.foodordering.dto.request.FoodRequest;
import com.example.foodordering.dto.response.ApiResponse;
import com.example.foodordering.dto.response.FoodResponse;
import com.example.foodordering.entity.Food;
import com.example.foodordering.mapper.FoodMapper;
import com.example.foodordering.repository.FoodRepository;
import com.example.foodordering.service.GuestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GuestServiceTest {

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private FoodMapper foodMapper;

    @InjectMocks
    private GuestService guestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAvailableDishes_WhenDishesAvailable() {
        List<Food> mockDishes = Arrays.asList(
                new Food(1, "Pizza", new BigDecimal("10.99"), "Cheese pizza", "pizza.jpg", true),
                new Food(2, "Burger", new BigDecimal("8.99"), "Beef burger", "burger.jpg", true)
        );
        List<FoodResponse> mockResponses = Arrays.asList(
                new FoodResponse(1, "Pizza", new BigDecimal("10.99"), "Cheese pizza", "pizza.jpg"),
                new FoodResponse(2, "Burger", new BigDecimal("8.99"), "Beef burger", "burger.jpg")
        );
        when(foodRepository.findByIsAvailableTrue()).thenReturn(mockDishes);
        when(foodMapper.toFoodResponse(any(Food.class))).thenAnswer(invocation -> {
            Food food = invocation.getArgument(0);
            return new FoodResponse(food.getFoodId(), food.getName(), food.getPrice(), food.getDescription(), food.getImage());
        });

        ApiResponse<List<FoodResponse>> response = guestService.getAvailableDishes();

        assertEquals(1000, response.getCode());
        assertEquals("Success", response.getMessage());
        assertEquals(2, response.getData().size());
    }

    @Test
    void testSearchDishes_WhenMatchesFound() {
        FoodRequest request = new FoodRequest();
        request.setKeyword("Pizza");
        List<Food> mockDishes = List.of(new Food(1, "Pizza", new BigDecimal("10.99"), "Cheese pizza", "pizza.jpg", true));
        List<FoodResponse> mockResponses = List.of(new FoodResponse(1, "Pizza", new BigDecimal("10.99"), "Cheese pizza", "pizza.jpg"));

        when(foodRepository.findByNameContainingIgnoreCase("Pizza")).thenReturn(mockDishes);
        when(foodMapper.toFoodResponse(any(Food.class))).thenAnswer(invocation -> {
            Food food = invocation.getArgument(0);
            return new FoodResponse(food.getFoodId(), food.getName(), food.getPrice(), food.getDescription(), food.getImage());
        });

        ApiResponse<List<FoodResponse>> response = guestService.searchDishes(request);

        assertEquals(1000, response.getCode());
        assertEquals("Success", response.getMessage());
        assertEquals(1, response.getData().size());
        assertEquals("Pizza", response.getData().get(0).getName());
    }

    @Test
    void testGetFoodDetails_WhenFoodExists() {
        Food mockFood = new Food(1, "Pizza", new BigDecimal("10.99"), "Cheese pizza", "pizza.jpg", true);
        FoodResponse mockResponse = new FoodResponse(1, "Pizza", new BigDecimal("10.99"), "Cheese pizza", "pizza.jpg");

        when(foodRepository.findById(1)).thenReturn(Optional.of(mockFood));
        when(foodMapper.toFoodResponse(mockFood)).thenReturn(mockResponse);

        ApiResponse<FoodResponse> response = guestService.getFoodDetails(1);

        assertEquals(1000, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals("Pizza", response.getData().getName());
    }
}

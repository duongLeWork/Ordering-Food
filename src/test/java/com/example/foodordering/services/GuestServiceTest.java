package com.example.foodordering.services;

import com.example.foodordering.dto.response.ApiResponse;
import com.example.foodordering.entity.Food;
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

    /** `
     * Creates a mock version of FoodRepository so we don’t interact with the real database.
     * Mockito will simulate database behavior for testing.
     * */
    @Mock
    private FoodRepository foodRepository;

    /**
     * Injects the mocked FoodRepository into GuestService automatically.
     * This way, guestService uses the mocked repository instead of a real one.
     * */
    @InjectMocks
    private GuestService guestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test: getAvailableDishes() returns available dishes.
     */
    @Test
    void testGetAvailableDishes_WhenDishesAvailable() {
        List<Food> mockDishes = Arrays.asList(
                new Food(1, "Pizza", new BigDecimal("10.99"), "Cheese pizza", "pizza.jpg", true),
                new Food(2, "Burger", new BigDecimal("8.99"), "Beef burger", "burger.jpg", true)
        );

        when(foodRepository.findByIsAvailableTrue()).thenReturn(mockDishes);

        ApiResponse<List<Food>> response = guestService.getAvailableDishes();

        assertEquals(1000, response.getCode());
        assertEquals("Success", response.getMessage());
        assertEquals(2, response.getData().size());
    }

    /**
     * Test: getAvailableDishes() returns 1404 when no dishes are available.
     */
    @Test
    void testGetAvailableDishes_WhenNoDishesAvailable() {
        when(foodRepository.findByIsAvailableTrue()).thenReturn(List.of());

        ApiResponse<List<Food>> response = guestService.getAvailableDishes();

        assertEquals(1404, response.getCode());
        assertEquals("Failed", response.getMessage());
        assertNull(response.getData());
    }

    /**
     * Test: searchDishes() returns matching dishes.
     */
    @Test
    void testSearchDishes_WhenMatchesFound() {
        List<Food> mockDishes = List.of(new Food(1, "Pizza", new BigDecimal("10.99"), "Cheese pizza", "pizza.jpg", true));
        when(foodRepository.findByNameContainingIgnoreCase("Pizza")).thenReturn(mockDishes);

        ApiResponse<List<Food>> response = guestService.searchDishes("Pizza");

        assertEquals(1000, response.getCode());
        assertEquals("Success", response.getMessage());
        assertEquals(1, response.getData().size());
        assertEquals("Pizza", response.getData().get(0).getName());
    }

    /**
     * Test: searchDishes() returns 1404 when no matches are found.
     */
    @Test
    void testSearchDishes_WhenNoMatchesFound() {
        when(foodRepository.findByNameContainingIgnoreCase("Unknown")).thenReturn(List.of());

        ApiResponse<List<Food>> response = guestService.searchDishes("Unknown");

        assertEquals(1404, response.getCode());
        assertEquals("Không tìm thấy món ăn nào!", response.getMessage());
        assertNull(response.getData());
    }

    /**
     * Test: getFoodDetails() returns food when found.
     */
    @Test
    void testGetFoodDetails_WhenFoodExists() {
        Food mockFood = new Food(1, "Pizza", new BigDecimal("10.99"), "Cheese pizza", "pizza.jpg", true);
        when(foodRepository.findById(1)).thenReturn(Optional.of(mockFood));

        ApiResponse<Food> response = guestService.getFoodDetails(1);

        assertEquals(1000, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals("Pizza", response.getData().getName());
        assertEquals("pizza.jpg", response.getData().getImage());
    }

    /**
     * Test: getFoodDetails() returns 1404 when food is not found.
     */
    @Test
    void testGetFoodDetails_WhenFoodNotFound() {
        when(foodRepository.findById(99)).thenReturn(Optional.empty());

        ApiResponse<Food> response = guestService.getFoodDetails(99);

        assertEquals(1404, response.getCode());
        assertEquals("Không tìm thấy món ăn!", response.getMessage());
        assertNull(response.getData());
    }

    /**
     * Test: recommendDishes() returns recommendations.
     */
    @Test
    void testRecommendDishes_WhenRecommendationsExist() {
        List<Food> mockDishes = List.of(new Food(1, "Pepperoni Pizza", new BigDecimal("12.99"), "Spicy pizza", "pepperoni.jpg", true));
        when(foodRepository.findSimilarDishes("Pizza")).thenReturn(mockDishes);

        ApiResponse<List<Food>> response = guestService.recommendDishes("Pizza");

        assertEquals(1000, response.getCode());
        assertEquals("Success", response.getMessage());
        assertEquals(1, response.getData().size());
        assertEquals("Pepperoni Pizza", response.getData().get(0).getName());
    }

    /**
     * Test: recommendDishes() returns 1404 when no recommendations are found.
     */
    @Test
    void testRecommendDishes_WhenNoRecommendationsExist() {
        when(foodRepository.findSimilarDishes("Unknown")).thenReturn(List.of());

        ApiResponse<List<Food>> response = guestService.recommendDishes("Unknown");

        assertEquals(1404, response.getCode());
        assertEquals("Không có gợi ý món ăn nào", response.getMessage());
        assertNull(response.getData());
    }
}

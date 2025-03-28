package com.example.foodordering.service;

import com.example.foodordering.dto.response.ApiResponse;
import com.example.foodordering.entity.Customer;
import com.example.foodordering.entity.Food;
import com.example.foodordering.entity.FoodOrder;
import com.example.foodordering.entity.OrderStatus;
import com.example.foodordering.repository.intf.CustomerRepository;
import com.example.foodordering.repository.FoodOrderRepository;
import com.example.foodordering.repository.FoodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManagerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private FoodOrderRepository foodOrderRepository;
    @Mock
    private FoodRepository foodRepository;

    @InjectMocks
    private ManagerService managerService;

    private Customer customer1;
    private Customer customer2;
    private Food food1;
    private Food food2;
    private FoodOrder order1;
    private FoodOrder order2;
    private OrderStatus placedOrderStatus;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer1 = new Customer();
        customer1.setId(1);

        customer2 = new Customer();
        customer2.setId(2);

        food1 = new Food();
        food1.setId(10);
        food1.setName("Burger");
        food1.setPrice(new BigDecimal("8.99"));
        food1.setIsAvailable(true);

        food2 = new Food();
        food2.setId(11);
        food2.setName("Pizza");
        food2.setPrice(new BigDecimal("12.99"));
        food2.setIsAvailable(true);

        placedOrderStatus = new OrderStatus();
        placedOrderStatus.setId(2);
        placedOrderStatus.setStatusValue(true);

        order1 = new FoodOrder();
        order1.setId(100);
        order1.setCustomer(customer1);
        order1.setOrderStatus(placedOrderStatus);

        order2 = new FoodOrder();
        order2.setId(101);
        order2.setCustomer(customer2);
        order2.setOrderStatus(placedOrderStatus);

        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1, customer2));
        when(foodOrderRepository.findByOrderStatus_StatusValue(true)).thenReturn(Arrays.asList(order1, order2));
        when(foodRepository.save(any(Food.class))).thenReturn(food1);
        when(foodRepository.findById(10)).thenReturn(Optional.of(food1));
        when(foodRepository.findById(99)).thenReturn(Optional.empty()); // For not found case
        when(foodRepository.save(food1)).thenReturn(food1); // For update and delete
    }

    @Test
    void getAllUsers_ReturnsListOfUsers() {
        ApiResponse<List<Customer>> response = managerService.getAllUsers();
        assertEquals(1000, response.getCode());
        assertEquals("Success", response.getMessage());
        assertEquals(2, response.getData().size());
    }

    @Test
    void getAllOrders_ReturnsListOfOrders() {
        ApiResponse<List<FoodOrder>> response = managerService.getAllOrders();
        assertEquals(1000, response.getCode());
        assertEquals("Success", response.getMessage());
        assertEquals(2, response.getData().size());
    }

    @Test
    void addFood_AddsNewFood() {
        Food newFood = new Food();
        newFood.setName("Sushi");
        when(foodRepository.save(newFood)).thenReturn(newFood);

        ApiResponse<Food> response = managerService.addFood(newFood);
        assertEquals(1201, response.getCode());
        assertEquals("Food added successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals("Sushi", response.getData().getName());
    }

    @Test
    void updateFood_UpdatesExistingFood() {
        Food updatedFood = new Food();
        updatedFood.setName("Updated Burger");
        updatedFood.setPrice(new BigDecimal("9.99"));
        updatedFood.setIsAvailable(false);
        when(foodRepository.findById(10)).thenReturn(Optional.of(food1));
        when(foodRepository.save(any(Food.class))).thenReturn(updatedFood);

        ApiResponse<Food> response = managerService.updateFood(10, updatedFood);
        assertEquals(1000, response.getCode());
        assertEquals("Food updated successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals("Updated Burger", response.getData().getName());
        assertEquals(new BigDecimal("9.99"), response.getData().getPrice());
        assertFalse(response.getData().getIsAvailable());
    }

    @Test
    void updateFood_FoodNotFound() {
        Food updatedFood = new Food();
        assertThrows(ResponseStatusException.class, () -> managerService.updateFood(99, updatedFood));
    }

    @Test
    void deleteFood_MarksFoodAsUnavailable() {
        ApiResponse<String> response = managerService.deleteFood(10);
        assertEquals(1000, response.getCode());
        assertEquals("Food item marked as unavailable", response.getMessage());
        assertEquals("Deleted", response.getData());
        assertTrue(!food1.getIsAvailable()); // Kiểm tra trạng thái sau khi gọi service
        verify(foodRepository, times(1)).save(food1);
    }

    @Test
    void deleteFood_FoodNotFound() {
        assertThrows(ResponseStatusException.class, () -> managerService.deleteFood(99));
    }

    @Test
    void getSalesStatistics_ReturnsListOfPlacedOrders() {
        ApiResponse<List<FoodOrder>> response = managerService.getSalesStatistics();
        assertEquals(1000, response.getCode());
        assertEquals("Success", response.getMessage());
        assertEquals(2, response.getData().size());
    }
}
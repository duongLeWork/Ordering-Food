package com.example.foodordering.service;

import com.example.foodordering.dto.request.CartItemRequest;
import com.example.foodordering.dto.response.ApiResponse;
import com.example.foodordering.entity.*;
import com.example.foodordering.repository.*;
import com.example.foodordering.repository.intf.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private OrderMenuItemRepository orderMenuItemRepository;
    @Mock
    private FoodRepository foodRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private FoodOrderRepository foodOrderRepository;
    @Mock
    private OrderStatusRepository orderStatusRepository;

    @InjectMocks
    private CartService cartService;

    private Customer customer;
    private Food food;
    private FoodOrder cartOrder;
    private OrderStatus cartOrderStatus;
    private OrderMenuItem cartItem;
    private CartItemRequest cartItemRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Khởi tạo dữ liệu mock mới cho mỗi test case
        customer = new Customer();
        customer.setId(1);

        food = new Food();
        food.setId(10);
        food.setName("Burger");
        food.setPrice(new BigDecimal("8.99"));

        cartOrderStatus = new OrderStatus();
        cartOrderStatus.setId(1);
        cartOrderStatus.setStatusValue(false); // Giả sử false là trạng thái giỏ hàng

        cartOrder = new FoodOrder();
        cartOrder.setId(100);
        cartOrder.setCustomer(customer);
        cartOrder.setOrderStatus(cartOrderStatus);
        cartOrder.setOrderMenuItems(Arrays.asList());

        cartItem = new OrderMenuItem();
        cartItem.setId(200);
        cartItem.setFood(food);
        cartItem.setQuantityOrdered(2);
        cartItem.setFoodOrder(cartOrder);

        cartItemRequest = new CartItemRequest();
        cartItemRequest.setCustomerId(customer.getId());
        cartItemRequest.setFoodId(food.getId());
        cartItemRequest.setQuantity(2);

        // Cấu hình mặc định cho repository methods nếu cần
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(foodRepository.findById(food.getId())).thenReturn(Optional.of(food));
        when(orderStatusRepository.findById(1)).thenReturn(Optional.of(cartOrderStatus));
        when(foodOrderRepository.findByCustomer_IdAndOrderStatus_StatusValue(customer.getId(), false))
                .thenReturn(Optional.of(cartOrder));
        when(orderMenuItemRepository.findByFoodOrder_Id(cartOrder.getId())).thenReturn(Arrays.asList(cartItem));
        when(orderMenuItemRepository.findById(cartItem.getId())).thenReturn(Optional.of(cartItem));
        when(foodOrderRepository.save(any(FoodOrder.class))).thenReturn(cartOrder);
        when(orderMenuItemRepository.save(any(OrderMenuItem.class))).thenReturn(cartItem);
        when(orderMenuItemRepository.existsById(cartItem.getId())).thenReturn(true);
    }

    @Test
    void getCart_WhenCartExistsAndHasItems_ReturnsSuccessWithCartItems() {
        ApiResponse<List<OrderMenuItem>> response = cartService.getCart(customer.getId());

        assertEquals(1000, response.getCode());
        assertEquals("Success", response.getMessage());
        assertEquals(1, response.getData().size());
        assertEquals(cartItem.getId(), response.getData().get(0).getId());
    }

    @Test
    void getCart_WhenCartExistsAndIsEmpty_ReturnsSuccessWithEmptyList() {
        when(orderMenuItemRepository.findByFoodOrder_Id(cartOrder.getId())).thenReturn(Arrays.asList());

        ApiResponse<List<OrderMenuItem>> response = cartService.getCart(customer.getId());

        assertEquals(1000, response.getCode());
        assertEquals("Success", response.getMessage());
        assertTrue(response.getData().isEmpty());
    }

    @Test
    void getCart_WhenCartDoesNotExist_ReturnsSuccessWithEmptyList() {
        when(foodOrderRepository.findByCustomer_IdAndOrderStatus_StatusValue(customer.getId(), false))
                .thenReturn(Optional.empty());

        ApiResponse<List<OrderMenuItem>> response = cartService.getCart(customer.getId());

        assertEquals(1000, response.getCode());
        assertEquals("Success", response.getMessage());
        assertTrue(response.getData().isEmpty());
    }

    @Test
    void addItemToCart_WhenCartDoesNotExist_CreatesNewCartAndAddsItem() {
        when(foodOrderRepository.findByCustomer_IdAndOrderStatus_StatusValue(customer.getId(), false))
                .thenReturn(Optional.empty());
        when(foodOrderRepository.save(any(FoodOrder.class))).thenReturn(cartOrder);
        when(orderMenuItemRepository.save(any(OrderMenuItem.class))).thenReturn(cartItem);

        ApiResponse<OrderMenuItem> response = cartService.addItemToCart(cartItemRequest);

        assertEquals(1201, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(food.getId(), response.getData().getFood().getId());
        assertEquals(cartItemRequest.getQuantity(), response.getData().getQuantityOrdered());
        verify(foodOrderRepository, times(1)).save(any(FoodOrder.class));
        verify(orderMenuItemRepository, times(1)).save(any(OrderMenuItem.class));
    }

    @Test
    void addItemToCart_WhenCartExists_AddsItemToExistingCart() {
        when(orderMenuItemRepository.save(any(OrderMenuItem.class))).thenReturn(cartItem);

        ApiResponse<OrderMenuItem> response = cartService.addItemToCart(cartItemRequest);

        assertEquals(1201, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(food.getId(), response.getData().getFood().getId());
        assertEquals(cartItemRequest.getQuantity(), response.getData().getQuantityOrdered());
        verify(foodOrderRepository, times(1)).findByCustomer_IdAndOrderStatus_StatusValue(customer.getId(), false);
        verify(foodOrderRepository, times(1)).save(any(FoodOrder.class)); // Should be called to update totalItems and price
        verify(orderMenuItemRepository, times(1)).save(any(OrderMenuItem.class));
    }

    @Test
    void updateItemQuantity_WhenItemExists_UpdatesQuantity() {
        int newQuantity = 3;
        when(orderMenuItemRepository.findById(cartItem.getId())).thenReturn(Optional.of(cartItem));
        when(orderMenuItemRepository.save(any(OrderMenuItem.class))).thenReturn(cartItem);
        when(foodOrderRepository.save(any(FoodOrder.class))).thenReturn(cartOrder);

        ApiResponse<OrderMenuItem> response = cartService.updateItemQuantity(cartItem.getId(), newQuantity);

        assertEquals(1000, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(newQuantity, response.getData().getQuantityOrdered());
        verify(orderMenuItemRepository, times(1)).findById(cartItem.getId());
        verify(orderMenuItemRepository, times(1)).save(any(OrderMenuItem.class));
        verify(foodOrderRepository, times(1)).save(any(FoodOrder.class)); // Should be called to update totalItems and price
    }

    @Test
    void updateItemQuantity_WhenItemDoesNotExist_ThrowsException() {
        when(orderMenuItemRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> cartService.updateItemQuantity(999, 3));
        verify(orderMenuItemRepository, times(1)).findById(anyInt());
        verify(orderMenuItemRepository, never()).save(any(OrderMenuItem.class));
        verify(foodOrderRepository, never()).save(any(FoodOrder.class));
    }

    @Test
    void removeItemFromCart_WhenItemExists_RemovesItem() {
        when(orderMenuItemRepository.existsById(cartItem.getId())).thenReturn(true);
        when(orderMenuItemRepository.findById(cartItem.getId())).thenReturn(Optional.of(cartItem));
        doNothing().when(orderMenuItemRepository).deleteById(cartItem.getId());
        when(foodOrderRepository.save(any(FoodOrder.class))).thenReturn(cartOrder);

        ApiResponse<String> response = cartService.removeItemFromCart(cartItem.getId());

        assertEquals(1000, response.getCode());
        assertEquals("Success", response.getMessage());
        assertEquals("Deleted", response.getData());
        verify(orderMenuItemRepository, times(1)).existsById(cartItem.getId());
        verify(orderMenuItemRepository, times(1)).deleteById(cartItem.getId());
        verify(foodOrderRepository, times(1)).save(any(FoodOrder.class)); // Should be called to update totalItems and price
    }

    @Test
    void removeItemFromCart_WhenItemDoesNotExist_ReturnsFailed() {
        when(orderMenuItemRepository.existsById(anyInt())).thenReturn(false);

        ApiResponse<String> response = cartService.removeItemFromCart(999);

        assertEquals(1404, response.getCode());
        assertEquals("Failed", response.getMessage());
        assertNull(response.getData());
        verify(orderMenuItemRepository, times(1)).existsById(anyInt());
        verify(orderMenuItemRepository, never()).deleteById(anyInt());
        verify(foodOrderRepository, never()).save(any(FoodOrder.class));
    }

    @Test
    void clearCart_WhenCartExists_ClearsAllItems() {
        when(foodOrderRepository.findByCustomer_IdAndOrderStatus_StatusValue(customer.getId(), false))
                .thenReturn(Optional.of(cartOrder));
        doNothing().when(orderMenuItemRepository).deleteByFoodOrder_Id(cartOrder.getId());
        when(foodOrderRepository.save(any(FoodOrder.class))).thenReturn(cartOrder);

        ApiResponse<String> response = cartService.clearCart(customer.getId());

        assertEquals(1000, response.getCode());
        assertEquals("Success", response.getMessage());
        assertEquals("Cart Cleared", response.getData());
        verify(foodOrderRepository, times(1)).findByCustomer_IdAndOrderStatus_StatusValue(customer.getId(), false);
        verify(orderMenuItemRepository, times(1)).deleteByFoodOrder_Id(cartOrder.getId());
        verify(foodOrderRepository, times(1)).save(any(FoodOrder.class)); // Should be called to reset totalItems and price
    }

    @Test
    void clearCart_WhenCartDoesNotExist_ReturnsFailed() {
        when(foodOrderRepository.findByCustomer_IdAndOrderStatus_StatusValue(customer.getId(), false))
                .thenReturn(Optional.empty());

        ApiResponse<String> response = cartService.clearCart(customer.getId());

        assertEquals(1404, response.getCode());
        assertEquals("Failed", response.getMessage());
        assertEquals("Cart not found for this customer", response.getData());
        verify(foodOrderRepository, times(1)).findByCustomer_IdAndOrderStatus_StatusValue(customer.getId(), false);
        verify(orderMenuItemRepository, never()).deleteByFoodOrder_Id(anyInt());
        verify(foodOrderRepository, never()).save(any(FoodOrder.class));
    }
}
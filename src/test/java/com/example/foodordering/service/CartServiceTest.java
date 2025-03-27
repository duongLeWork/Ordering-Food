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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
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
    private Food food1;
    private Food food2;
    private FoodOrder cartOrder;
    private OrderStatus cartOrderStatus;
    private OrderMenuItem cartItem1;
    private OrderMenuItem cartItem2;
    private CartItemRequest cartItemRequest1;
    private CartItemRequest cartItemRequest2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Khởi tạo dữ liệu mock mới cho mỗi test case
        customer = new Customer();
        customer.setId(1);

        food1 = new Food();
        food1.setId(10);
        food1.setName("Burger");
        food1.setPrice(new BigDecimal("8.99"));

        food2 = new Food();
        food2.setId(11);
        food2.setName("Pizza");
        food2.setPrice(new BigDecimal("12.99"));

        cartOrderStatus = new OrderStatus();
        cartOrderStatus.setId(1);
        cartOrderStatus.setStatusValue(false); // Giả sử false là trạng thái giỏ hàng

        cartOrder = new FoodOrder();
        cartOrder.setId(100);
        cartOrder.setCustomer(customer);
        cartOrder.setOrderStatus(cartOrderStatus);
        cartOrder.setOrderMenuItems(Arrays.asList()); // Khởi tạo rỗng

        cartItem1 = new OrderMenuItem();
        cartItem1.setId(200);
        cartItem1.setFood(food1);
        cartItem1.setQuantityOrdered(2);
        cartItem1.setFoodOrder(cartOrder);

        cartItem2 = new OrderMenuItem();
        cartItem2.setId(201);
        cartItem2.setFood(food2);
        cartItem2.setQuantityOrdered(1);
        cartItem2.setFoodOrder(cartOrder);

        cartItemRequest1 = new CartItemRequest();
        cartItemRequest1.setCustomerId(customer.getId());
        cartItemRequest1.setFoodId(food1.getId());
        cartItemRequest1.setQuantity(2);

        cartItemRequest2 = new CartItemRequest();
        cartItemRequest2.setCustomerId(customer.getId());
        cartItemRequest2.setFoodId(food2.getId());
        cartItemRequest2.setQuantity(1);

        // Cấu hình mặc định cho repository methods
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(foodRepository.findById(food1.getId())).thenReturn(Optional.of(food1));
        when(foodRepository.findById(food2.getId())).thenReturn(Optional.of(food2));
        when(orderStatusRepository.findById(1)).thenReturn(Optional.of(cartOrderStatus));
        when(foodOrderRepository.findByCustomer_IdAndOrderStatus_StatusValue(customer.getId(), false))
                .thenReturn(Optional.of(cartOrder));
        when(foodOrderRepository.save(any(FoodOrder.class))).thenReturn(cartOrder);
        when(orderMenuItemRepository.save(any(OrderMenuItem.class))).thenReturn(cartItem1).thenReturn(cartItem2); // Cần cẩn thận với thứ tự trả về
        when(orderMenuItemRepository.existsById(anyInt())).thenReturn(true);
        when(orderMenuItemRepository.findById(cartItem1.getId())).thenReturn(Optional.of(cartItem1));
        when(orderMenuItemRepository.findById(cartItem2.getId())).thenReturn(Optional.of(cartItem2));
        doNothing().when(orderMenuItemRepository).deleteById(anyInt());
        doNothing().when(orderMenuItemRepository).deleteByFoodOrder_Id(anyInt());
    }

    @Test
    void getCart_ReturnsSuccessWithCartItems() {
        cartOrder.setOrderMenuItems(Arrays.asList(cartItem1, cartItem2));
        when(orderMenuItemRepository.findByFoodOrder_Id(cartOrder.getId())).thenReturn(Arrays.asList(cartItem1, cartItem2));

        ApiResponse<List<OrderMenuItem>> response = cartService.getCart(customer.getId());

        assertEquals(1000, response.getCode());
        assertEquals("Success", response.getMessage());
        assertEquals(2, response.getData().size());
    }

    @Test
    void addItemToCart_AddsNewItemToExistingCart() {
        cartOrder.setOrderMenuItems(new java.util.ArrayList<>()); // Giỏ hàng ban đầu rỗng
        when(orderMenuItemRepository.save(any(OrderMenuItem.class))).thenReturn(cartItem1); // Chỉ định trả về cho lần thêm đầu tiên

        ApiResponse<OrderMenuItem> response = cartService.addItemToCart(cartItemRequest1);

        assertEquals(1201, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(food1.getId(), response.getData().getFood().getId());
        assertEquals(cartItemRequest1.getQuantity(), response.getData().getQuantityOrdered());
        verify(foodOrderRepository, times(1)).save(any(FoodOrder.class)); // Để cập nhật tổng
        verify(orderMenuItemRepository, times(1)).save(any(OrderMenuItem.class));
    }

    @Test
    void updateItemQuantity_UpdatesQuantityOfExistingItem() {
        int newQuantity = 3;
        cartOrder.setOrderMenuItems(Arrays.asList(cartItem1));
        when(orderMenuItemRepository.findById(cartItem1.getId())).thenReturn(Optional.of(cartItem1));
        when(orderMenuItemRepository.save(any(OrderMenuItem.class))).thenReturn(cartItem1);
        when(foodOrderRepository.save(any(FoodOrder.class))).thenReturn(cartOrder);

        ApiResponse<OrderMenuItem> response = cartService.updateItemQuantity(cartItem1.getId(), newQuantity);

        assertEquals(1000, response.getCode());
        assertEquals("Success", response.getMessage());
        assertEquals(newQuantity, response.getData().getQuantityOrdered());
        verify(orderMenuItemRepository, times(1)).findById(cartItem1.getId());
        verify(orderMenuItemRepository, times(1)).save(any(OrderMenuItem.class));
        verify(foodOrderRepository, times(1)).save(any(FoodOrder.class)); // Để cập nhật tổng
    }

    @Test
    void removeItemFromCart_RemovesExistingItem() {
        cartOrder.setOrderMenuItems(Arrays.asList(cartItem1));
        when(orderMenuItemRepository.findById(cartItem1.getId())).thenReturn(Optional.of(cartItem1));
        doNothing().when(orderMenuItemRepository).deleteById(cartItem1.getId());
        when(foodOrderRepository.save(any(FoodOrder.class))).thenReturn(cartOrder);

        ApiResponse<String> response = cartService.removeItemFromCart(cartItem1.getId());

        assertEquals(1000, response.getCode());
        assertEquals("Success", response.getMessage());
        assertEquals("Deleted", response.getData());
        verify(orderMenuItemRepository, times(1)).findById(cartItem1.getId()); // Verify findById is called
        verify(orderMenuItemRepository, times(1)).deleteById(cartItem1.getId()); // Verify deleteById is called
        verify(foodOrderRepository, times(1)).save(any(FoodOrder.class)); // Để cập nhật tổng
    }

    @Test
    void clearCart_ClearsAllItemsFromCart() {
        cartOrder.setOrderMenuItems(Arrays.asList(cartItem1, cartItem2));
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
        verify(foodOrderRepository, times(1)).save(any(FoodOrder.class)); // Để cập nhật tổng
    }
}
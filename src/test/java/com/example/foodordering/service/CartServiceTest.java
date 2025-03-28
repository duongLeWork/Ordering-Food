//package com.example.foodordering.service;
//
//import com.example.foodordering.dto.request.CartItemRequest;
//import com.example.foodordering.dto.response.ApiResponse;
//import com.example.foodordering.entity.*;
//import com.example.foodordering.repository.*;
//import com.example.foodordering.repository.intf.CustomerRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
///**
// * Unit tests for CartService.
// */
//class CartServiceTest {
//
//    @Mock private OrderMenuItemRepository orderMenuItemRepository;
//    @Mock private FoodRepository foodRepository;
//    @Mock private CustomerRepository customerRepository;
//    @Mock private FoodOrderRepository foodOrderRepository;
//    @Mock private OrderStatusRepository orderStatusRepository;
//
//    @InjectMocks private CartService cartService;
//
//    private Customer customer;
//    private Food food;
//    private FoodOrder cartOrder;
//    private OrderStatus cartOrderStatus;
//    private OrderMenuItem cartItem;
//    private CartItemRequest cartItemRequest;
//
//    /**
//     * Sets up test data before each test case runs.
//     */
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        customer = new Customer(); customer.setId(1);
//        food = new Food(); food.setId(10); food.setPrice(new BigDecimal("8.99"));
//        cartOrderStatus = new OrderStatus(); cartOrderStatus.setId(1); cartOrderStatus.setStatusValue(false);
//        cartOrder = new FoodOrder(); cartOrder.setId(100); cartOrder.setCustomer(customer); cartOrder.setOrderStatus(cartOrderStatus);
//        cartItem = new OrderMenuItem(); cartItem.setId(200); cartItem.setFood(food); cartItem.setQuantityOrdered(2); cartItem.setFoodOrder(cartOrder);
//        cartItemRequest = new CartItemRequest(); cartItemRequest.setCustomerId(1); cartItemRequest.setFoodId(10); cartItemRequest.setQuantity(2);
//
//        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
//        when(foodRepository.findById(10)).thenReturn(Optional.of(food));
//        when(orderStatusRepository.findById(1)).thenReturn(Optional.of(cartOrderStatus));
//        when(foodOrderRepository.save(any(FoodOrder.class))).thenReturn(cartOrder);
//        when(orderMenuItemRepository.save(any(OrderMenuItem.class))).thenReturn(cartItem);
//        when(orderMenuItemRepository.findById(200)).thenReturn(Optional.of(cartItem));
//        doNothing().when(orderMenuItemRepository).deleteById(anyInt());
//    }
//
//    @Test
//    void getCart_ReturnsCartItems() {
//        when(orderMenuItemRepository.findByFoodOrder_Id(100)).thenReturn(List.of(cartItem));
//        ApiResponse<List<OrderMenuItem>> response = cartService.getCart(1);
//        assertEquals(1000, response.getCode());
//        assertEquals(1, response.getData().size());
//    }
//
//    @Test
//    void getCart_CustomerNotFound() {
//        when(customerRepository.findById(2)).thenReturn(Optional.empty());
//        ApiResponse<List<OrderMenuItem>> response = cartService.getCart(2);
//        assertEquals(4000, response.getCode());
//    }
//
//    @Test
//    void addItemToCart_AddsItemSuccessfully() {
//        ApiResponse<OrderMenuItem> response = cartService.addItemToCart(cartItemRequest);
//        assertEquals(1201, response.getCode());
//        assertNotNull(response.getData());
//    }
//
//    @Test
//    void addItemToCart_CustomerNotFound() {
//        when(customerRepository.findById(1)).thenReturn(Optional.empty());
//        ApiResponse<OrderMenuItem> response = cartService.addItemToCart(cartItemRequest);
//        assertEquals(4000, response.getCode());
//    }
//
//    @Test
//    void addItemToCart_FoodNotFound() {
//        when(foodRepository.findById(10)).thenReturn(Optional.empty());
//        ApiResponse<OrderMenuItem> response = cartService.addItemToCart(cartItemRequest);
//        assertEquals(4001, response.getCode());
//    }
//
//    @Test
//    void updateItemQuantity_ChangesQuantity() {
//        when(orderMenuItemRepository.findById(200)).thenReturn(Optional.of(cartItem));
//        ApiResponse<OrderMenuItem> response = cartService.updateItemQuantity(200, 3);
//        assertEquals(1000, response.getCode());
//        assertEquals(3, response.getData().getQuantityOrdered());
//    }
//
//    @Test
//    void updateItemQuantity_ItemNotFound() {
//        when(orderMenuItemRepository.findById(300)).thenReturn(Optional.empty());
//        ApiResponse<OrderMenuItem> response = cartService.updateItemQuantity(300, 3);
//        assertEquals(4002, response.getCode());
//    }
//
//    @Test
//    void removeItemFromCart_RemovesItem() {
//        ApiResponse<String> response = cartService.removeItemFromCart(200);
//        assertEquals(1000, response.getCode());
//        verify(orderMenuItemRepository).deleteById(200);
//    }
//
//    @Test
//    void removeItemFromCart_ItemNotFound() {
//        when(orderMenuItemRepository.findById(300)).thenReturn(Optional.empty());
//        ApiResponse<String> response = cartService.removeItemFromCart(300);
//        assertEquals(4002, response.getCode());
//    }
//
//    @Test
//    void clearCart_RemovesAllItems() {
//        doNothing().when(orderMenuItemRepository).deleteByFoodOrder_Id(100);
//        ApiResponse<String> response = cartService.clearCart(1);
//        assertEquals(1000, response.getCode());
//    }
//
//    @Test
//    void clearCart_CustomerNotFound() {
//        when(customerRepository.findById(2)).thenReturn(Optional.empty());
//        ApiResponse<String> response = cartService.clearCart(2);
//        assertEquals(4000, response.getCode());
//    }
//}
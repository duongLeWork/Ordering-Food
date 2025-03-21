package com.example.foodordering.service;

import com.example.foodordering.dto.request.CartItemRequest;
import com.example.foodordering.entity.Customer;
import com.example.foodordering.entity.Food;
import com.example.foodordering.entity.OrderMenuItem;
import com.example.foodordering.repository.CustomerRepository;
import com.example.foodordering.repository.FoodRepository;
import com.example.foodordering.repository.OrderMenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private OrderMenuItemRepository orderMenuItemRepository;

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddItemToCart_Success() {
        Customer customer = new Customer();
        customer.setCustomerId(1);

        Food food = new Food();
        food.setFoodId(1);
        food.setName("Pizza");

        CartItemRequest request = new CartItemRequest();
        request.setCustomerId(1);
        request.setFoodId(1);
        request.setQuantity(2);

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(foodRepository.findById(1)).thenReturn(Optional.of(food));

        String result = cartService.addItemToCart(request);

        assertEquals("Đã thêm món vào giỏ hàng!", result);
        verify(orderMenuItemRepository, times(1)).save(any(OrderMenuItem.class));
    }

    @Test
    void testRemoveItemFromCart_Success() {
        when(orderMenuItemRepository.existsById(1)).thenReturn(true);

        String result = cartService.removeItemFromCart(1);

        assertEquals("Đã xóa món khỏi giỏ hàng!", result);
        verify(orderMenuItemRepository, times(1)).deleteById(1);
    }

    @Test
    void testClearCart_Success() {
        String result = cartService.clearCart(1);

        assertEquals("Đã xóa toàn bộ giỏ hàng!", result);
        verify(orderMenuItemRepository, times(1)).deleteByCustomerCustomerId(1);
    }
}

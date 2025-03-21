package com.example.foodordering.service;

import com.example.foodordering.dto.request.OrderRequest;
import com.example.foodordering.entity.Customer;
import com.example.foodordering.entity.FoodOrder;
import com.example.foodordering.entity.OrderStatus;
import com.example.foodordering.repository.CustomerRepository;
import com.example.foodordering.repository.FoodOrderRepository;
import com.example.foodordering.repository.OrderStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private FoodOrderRepository foodOrderRepository;

    @Mock
    private OrderStatusRepository orderStatusRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateProfile_Success() {
        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerId(1);
        existingCustomer.setFirstName("John");
        existingCustomer.setLastName("Doe");

        Customer updatedCustomer = new Customer();
        updatedCustomer.setFirstName("Johnathan");
        updatedCustomer.setLastName("Doe");

        when(customerRepository.findById(1)).thenReturn(Optional.of(existingCustomer));

        String result = customerService.updateProfile(1, updatedCustomer);

        assertEquals("Cập nhật thông tin thành công!", result);
        verify(customerRepository, times(1)).save(existingCustomer);
    }

    @Test
    void testUpdateProfile_Fail_NotFound() {
        when(customerRepository.findById(99)).thenReturn(Optional.empty());

        String result = customerService.updateProfile(99, new Customer());

        assertEquals("Không tìm thấy khách hàng!", result);
        verify(customerRepository, never()).save(any());
    }

    @Test
    void testPlaceOrder_Success() {
        Customer customer = new Customer();
        customer.setCustomerId(1);

        OrderStatus pendingStatus = new OrderStatus();
        pendingStatus.setStatusValue("Pending");

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomerId(1);
        orderRequest.setTotalAmount(new BigDecimal("22.99"));

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(orderStatusRepository.findByStatusValue("Pending")).thenReturn(Optional.of(pendingStatus));

        String result = customerService.placeOrder(orderRequest);

        assertEquals("Đơn hàng đã được tạo thành công!", result);
        verify(foodOrderRepository, times(1)).save(any(FoodOrder.class));
    }

    @Test
    void testTrackOrder_Success() {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setStatusValue("Processing");

        FoodOrder foodOrder = new FoodOrder();
        foodOrder.setFoodOrderId(1);
        foodOrder.setOrderStatus(orderStatus);

        when(foodOrderRepository.findById(1)).thenReturn(Optional.of(foodOrder));

        String result = customerService.trackOrder(1);

        assertEquals("Trạng thái đơn hàng: Processing", result);
    }

    @Test
    void testTrackOrder_Fail_NotFound() {
        when(foodOrderRepository.findById(99)).thenReturn(Optional.empty());

        String result = customerService.trackOrder(99);

        assertEquals("Không tìm thấy đơn hàng!", result);
    }
}

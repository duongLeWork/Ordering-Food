package com.example.foodordering.service;

import com.example.foodordering.dto.request.OrderRequest;
import com.example.foodordering.dto.request.UpdateOrderStatusRequest;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private FoodOrderRepository foodOrderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderStatusRepository orderStatusRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder_Success() {
        Customer customer = new Customer();
        customer.setCustomerId(1);

        OrderStatus pendingStatus = new OrderStatus();
        pendingStatus.setStatusValue("Pending");

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomerId(1);
        orderRequest.setTotalAmount(new BigDecimal("25.50"));

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(orderStatusRepository.findByStatusValue("Pending")).thenReturn(Optional.of(pendingStatus));

        String result = orderService.createOrder(orderRequest);

        assertEquals("Đơn hàng đã được tạo thành công!", result);
        verify(foodOrderRepository, times(1)).save(any(FoodOrder.class));
    }

    @Test
    void testUpdateOrderStatus_Success() {
        FoodOrder order = new FoodOrder();
        order.setFoodOrderId(1);

        OrderStatus completedStatus = new OrderStatus();
        completedStatus.setStatusValue("Completed");

        UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
        request.setOrderId(1);
        request.setNewStatus("Completed");

        when(foodOrderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderStatusRepository.findByStatusValue("Completed")).thenReturn(Optional.of(completedStatus));

        String result = orderService.updateOrderStatus(request);

        assertEquals("Cập nhật trạng thái đơn hàng thành công!", result);
        verify(foodOrderRepository, times(1)).save(order);
    }

    @Test
    void testGetOrdersByCustomer() {
        FoodOrder order = new FoodOrder();
        order.setFoodOrderId(1);
        when(foodOrderRepository.findByCustomerCustomerId(1)).thenReturn(List.of(order));

        List<FoodOrder> orders = orderService.getOrdersByCustomer(1);

        assertEquals(1, orders.size());
    }
}

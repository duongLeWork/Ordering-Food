//package com.example.foodordering.service;
//
//import com.example.foodordering.dto.response.ApiResponse;
//import com.example.foodordering.entity.*;
//import com.example.foodordering.repository.FoodOrderRepository;
//import com.example.foodordering.repository.OrderStatusRepository;
//import com.example.foodordering.repository.OrderMenuItemRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class OrderServiceTest {
//
//    @Mock
//    private FoodOrderRepository foodOrderRepository;
//    @Mock
//    private OrderStatusRepository orderStatusRepository;
//    @Mock
//    private OrderMenuItemRepository orderMenuItemRepository;
//    @Mock
//    private CartService cartService;
//
//    @InjectMocks
//    private OrderService orderService;
//
//    private Customer customer;
//    private Food food1;
//    private Food food2;
//    private FoodOrder cartOrder;
//    private FoodOrder placedOrder1;
//    private FoodOrder placedOrder2;
//    private OrderStatus cartOrderStatus;
//    private OrderStatus placedOrderStatus;
//    private OrderMenuItem cartItem1;
//    private OrderMenuItem cartItem2;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        customer = new Customer();
//        customer.setId(1);
//
//        food1 = new Food();
//        food1.setId(10);
//        food1.setName("Burger");
//        food1.setPrice(new BigDecimal("8.99"));
//
//        food2 = new Food();
//        food2.setId(11);
//        food2.setName("Pizza");
//        food2.setPrice(new BigDecimal("12.99"));
//
//        cartOrderStatus = new OrderStatus();
//        cartOrderStatus.setId(1);
//        cartOrderStatus.setStatusValue(false); // Trạng thái giỏ hàng
//
//        placedOrderStatus = new OrderStatus();
//        placedOrderStatus.setId(2);
//        placedOrderStatus.setStatusValue(true); // Trạng thái đã đặt hàng
//
//        cartOrder = new FoodOrder();
//        cartOrder.setId(100);
//        cartOrder.setCustomer(customer);
//        cartOrder.setOrderStatus(cartOrderStatus);
//        cartOrder.setTotalItems(2);
//        cartOrder.setPrice(new BigDecimal("20.98"));
//        cartOrder.setOrderMenuItems(Arrays.asList());
//
//        placedOrder1 = new FoodOrder();
//        placedOrder1.setId(200);
//        placedOrder1.setCustomer(customer);
//        placedOrder1.setOrderStatus(placedOrderStatus);
//        placedOrder1.setTotalItems(1);
//        placedOrder1.setPrice(new BigDecimal("8.99"));
//        placedOrder1.setOrderMenuItems(Arrays.asList());
//
//        placedOrder2 = new FoodOrder();
//        placedOrder2.setId(201);
//        placedOrder2.setCustomer(customer);
//        placedOrder2.setOrderStatus(placedOrderStatus);
//        placedOrder2.setTotalItems(2);
//        placedOrder2.setPrice(new BigDecimal("24.98"));
//        placedOrder2.setOrderMenuItems(Arrays.asList());
//
//        cartItem1 = new OrderMenuItem();
//        cartItem1.setId(300);
//        cartItem1.setFood(food1);
//        cartItem1.setQuantityOrdered(2);
//        cartItem1.setFoodOrder(cartOrder);
//
//        cartItem2 = new OrderMenuItem();
//        cartItem2.setId(301);
//        cartItem2.setFood(food2);
//        cartItem2.setQuantityOrdered(1);
//        cartItem2.setFoodOrder(cartOrder);
//
//        cartOrder.setOrderMenuItems(Arrays.asList(cartItem1, cartItem2));
//        placedOrder1.setOrderMenuItems(Arrays.asList(cartItem1));
//        placedOrder2.setOrderMenuItems(Arrays.asList(cartItem1, cartItem2));
//
//        when(orderStatusRepository.findByStatusValue(false)).thenReturn(Optional.of(cartOrderStatus));
//        when(orderStatusRepository.findByStatusValue(true)).thenReturn(Optional.of(placedOrderStatus));
//        when(foodOrderRepository.save(any(FoodOrder.class))).thenReturn(placedOrder1).thenReturn(placedOrder2); // Cần cẩn thận thứ tự
//        when(orderMenuItemRepository.save(any(OrderMenuItem.class))).thenReturn(cartItem1).thenReturn(cartItem2);
//        when(foodOrderRepository.findById(200)).thenReturn(Optional.of(placedOrder1));
//        when(foodOrderRepository.findById(201)).thenReturn(Optional.of(placedOrder2));
//        when(foodOrderRepository.findByCustomer_idAndOrderStatus_StatusValue(customer.getId(), true))
//                .thenReturn(Arrays.asList(placedOrder1, placedOrder2));
//        when(cartService.getCart(customer.getId())).thenReturn(ApiResponse.build(1000, "Success", Arrays.asList(cartItem1, cartItem2)));
//        doNothing().when(cartService).clearCart(customer.getId());
//    }
//
//    @Test
//    void createOrder_WhenCartIsNotEmpty_CreatesNewOrderAndClearsCart() {
//        ApiResponse<FoodOrder> response = orderService.createOrder(customer.getId());
//
//        assertEquals(1201, response.getCode());
//        assertEquals("Order created successfully", response.getMessage());
//        assertNotNull(response.getData());
//        assertEquals(customer.getId(), response.getData().getCustomer().getId());
//        assertEquals(placedOrderStatus.getId(), response.getData().getOrderStatus().getId());
//        assertEquals(cartOrder.getTotalItems(), response.getData().getTotalItems());
//        assertEquals(cartOrder.getPrice(), response.getData().getPrice());
//        assertEquals(cartOrder.getOrderMenuItems().size(), response.getData().getOrderMenuItems().size());
//        verify(cartService, times(1)).getCart(customer.getId());
//        verify(foodOrderRepository, times(1)).save(any(FoodOrder.class));
//        verify(orderMenuItemRepository, times(2)).save(any(OrderMenuItem.class)); // Số lượng item trong giỏ
//        verify(cartService, times(1)).clearCart(customer.getId());
//    }
//
//    @Test
//    void createOrder_WhenCartIsEmpty_ThrowsBadRequest() {
//        when(cartService.getCart(customer.getId())).thenReturn(ApiResponse.build(1000, "Success", Collections.emptyList()));
//
//        assertThrows(ResponseStatusException.class, () -> orderService.createOrder(customer.getId()));
//        verify(cartService, times(1)).getCart(customer.getId());
//        verify(foodOrderRepository, never()).save(any(FoodOrder.class));
//        verify(orderMenuItemRepository, never()).save(any(OrderMenuItem.class));
//        verify(cartService, never()).clearCart(customer.getId());
//    }
//
//    @Test
//    void getOrderList_WhenOrdersExistForCustomer_ReturnsListOfOrders() {
//        ApiResponse<List<FoodOrder>> response = orderService.getOrderList(customer.getId());
//
//        assertEquals(1000, response.getCode());
//        assertEquals("Success", response.getMessage());
//        assertEquals(2, response.getData().size());
//        assertEquals(placedOrder1.getId(), response.getData().get(0).getId());
//        assertEquals(placedOrder2.getId(), response.getData().get(1).getId());
//        verify(foodOrderRepository, times(1)).findByCustomer_idAndOrderStatus_StatusValue(customer.getId(), true);
//    }
//
//    @Test
//    void getOrderList_WhenNoOrdersExistForCustomer_ReturnsEmptyList() {
//        when(foodOrderRepository.findByCustomer_idAndOrderStatus_StatusValue(customer.getId(), true))
//                .thenReturn(Collections.emptyList());
//
//        ApiResponse<List<FoodOrder>> response = orderService.getOrderList(customer.getId());
//
//        assertEquals(1000, response.getCode());
//        assertEquals("Success", response.getMessage());
//        assertTrue(response.getData().isEmpty());
//        verify(foodOrderRepository, times(1)).findByCustomer_idAndOrderStatus_StatusValue(customer.getId(), true);
//    }
//
//    @Test
//    void getOrderDetails_WhenOrderExistsAndBelongsToCustomer_ReturnsOrderDetails() {
//        ApiResponse<FoodOrder> response = orderService.getOrderDetails(200, customer.getId());
//
//        assertEquals(1000, response.getCode());
//        assertEquals("Success", response.getMessage());
//        assertNotNull(response.getData());
//        assertEquals(placedOrder1.getId(), response.getData().getId());
//        assertEquals(customer.getId(), response.getData().getCustomer().getId());
//        verify(foodOrderRepository, times(1)).findById(200);
//    }
//
//    @Test
//    void getOrderDetails_WhenOrderDoesNotExist_ReturnsNotFound() {
//        when(foodOrderRepository.findById(999)).thenReturn(Optional.empty());
//
//        ApiResponse<FoodOrder> response = orderService.getOrderDetails(999, customer.getId());
//
//        assertEquals(1404, response.getCode());
//        assertEquals("Order not found", response.getMessage());
//        assertNull(response.getData());
//        verify(foodOrderRepository, times(1)).findById(999);
//    }
//
//    @Test
//    void getOrderDetails_WhenOrderExistsButDoesNotBelongToCustomer_ReturnsAccessDenied() {
//        Customer anotherCustomer = new Customer();
//        anotherCustomer.setId(2);
//        placedOrder1.setCustomer(anotherCustomer);
//        when(foodOrderRepository.findById(200)).thenReturn(Optional.of(placedOrder1));
//
//        ApiResponse<FoodOrder> response = orderService.getOrderDetails(200, customer.getId());
//
//        assertEquals(1403, response.getCode());
//        assertEquals("Access denied. Order does not belong to this customer.", response.getMessage());
//        assertNull(response.getData());
//        verify(foodOrderRepository, times(1)).findById(200);
//    }
//}
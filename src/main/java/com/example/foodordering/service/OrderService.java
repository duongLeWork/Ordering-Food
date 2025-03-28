package com.example.foodordering.service;

import com.example.foodordering.dto.response.ApiResponse;
import com.example.foodordering.entity.FoodOrder;
import com.example.foodordering.entity.OrderStatus;
import com.example.foodordering.entity.OrderMenuItem;
import com.example.foodordering.repository.FoodOrderRepository;
import com.example.foodordering.repository.OrderStatusRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final FoodOrderRepository foodOrderRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final CartService cartService; // Inject CartService để lấy giỏ hàng

    public OrderService(FoodOrderRepository foodOrderRepository, OrderStatusRepository orderStatusRepository, CartService cartService) {
        this.foodOrderRepository = foodOrderRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.cartService = cartService;
    }

    /**
     * Creates a new order from the customer's cart.
     *
     * @param customerId ID of the customer placing the order.
     * @return ApiResponse containing the newly created FoodOrder.
     */
    public ApiResponse<FoodOrder> createOrder(int customerId) {
        // Lấy giỏ hàng hiện tại của khách hàng
        ApiResponse<List<OrderMenuItem>> cartResponse = cartService.getCart(customerId);
        if (cartResponse.getData().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty. Cannot create order.");
        }
        List<OrderMenuItem> cartItems = cartResponse.getData();

        // Tìm trạng thái đơn hàng "Đã đặt" (giả sử statusValue là true)
        OrderStatus placedOrderStatus = orderStatusRepository.findByStatusValue(true)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Order Status 'Placed' not found."));

        // Tạo một FoodOrder mới
        FoodOrder newOrder = new FoodOrder();
        newOrder.setCustomer(cartItems.get(0).getFoodOrder().getCustomer()); // Lấy thông tin khách hàng từ giỏ hàng
        newOrder.setOrderStatus(placedOrderStatus);
//        newOrder.setOrderDate(LocalDateTime.now());
        newOrder.setTotalItems(cartItems.stream().mapToInt(OrderMenuItem::getQuantityOrdered).sum());
        newOrder.setPrice(cartItems.stream()
                .map(item -> item.getFood().getPrice().multiply(BigDecimal.valueOf(item.getQuantityOrdered())))
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add));

        // Sao chép các OrderMenuItem từ giỏ hàng sang đơn hàng mới
        List<OrderMenuItem> orderItems = cartItems.stream()
                .map(cartItem -> {
                    OrderMenuItem orderItem = new OrderMenuItem();
                    orderItem.setFood(cartItem.getFood());
                    orderItem.setQuantityOrdered(cartItem.getQuantityOrdered());
                    orderItem.setFoodOrder(newOrder); // Liên kết với đơn hàng mới
                    return orderItem;
                })
                .collect(Collectors.toList());
        newOrder.setOrderMenuItems(orderItems);

        // Lưu đơn hàng mới
        FoodOrder savedOrder = foodOrderRepository.save(newOrder);

        // Xóa các mục trong giỏ hàng của khách hàng sau khi đặt hàng thành công (tùy theo logic của bạn)
        cartService.clearCart(customerId);

        return ApiResponse.build(1201, "Order created successfully", savedOrder);
    }

    /**
     * Retrieves a list of orders placed by a specific customer.
     *
     * @param customerId ID of the customer.
     * @return ApiResponse containing a list of FoodOrder.
     */
    public ApiResponse<List<FoodOrder>> getOrderList(int customerId) {
        List<FoodOrder> orders = foodOrderRepository.findByCustomer_IdAndOrderStatus_StatusValue(customerId, true);
        return ApiResponse.build(1000, "Success", orders);
    }

    /**
     * Retrieves details of a specific order.
     *
     * @param orderId ID of the order.
     * @param customerId ID of the customer (to verify ownership).
     * @return ApiResponse containing the FoodOrder details.
     */
    public ApiResponse<FoodOrder> getOrderDetails(int orderId, int customerId) {
        Optional<FoodOrder> order = foodOrderRepository.findById(orderId);
        if (order.isEmpty()) {
            return ApiResponse.build(1404, "Order not found", null);
        }
        if (!order.get().getCustomer().getId().equals(customerId)) {
            return ApiResponse.build(1403, "Access denied. Order does not belong to this customer.", null);
        }
        return ApiResponse.build(1000, "Success", order.get());
    }
}
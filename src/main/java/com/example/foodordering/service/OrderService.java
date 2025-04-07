package com.example.foodordering.service;

import com.example.foodordering.dto.response.ApiResponse;
import com.example.foodordering.entity.Customer;
import com.example.foodordering.entity.FoodOrder;
import com.example.foodordering.entity.OrderMenuItem;
import com.example.foodordering.repository.FoodOrderRepository;
import com.example.foodordering.repository.intf.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final FoodOrderRepository foodOrderRepository;

    public OrderService(FoodOrderRepository foodOrderRepository, CartService cartService, CustomerRepository customerRepository) {
        this.foodOrderRepository = foodOrderRepository;
    }

    /**
     * Creates a new order from the customer's cart.
     * @return ApiResponse containing the newly created FoodOrder.
     */
    public FoodOrder createOrder(int customerId) {
        // Lấy đơn hàng trong giỏ hàng (orderStatus = false) của khách hàng
        Optional<FoodOrder> cartOrder = foodOrderRepository.findByCustomer_IdAndOrderStatus(customerId, false).stream().findFirst();

        // Kiểm tra nếu tìm thấy giỏ hàng
        if (cartOrder.isPresent()) {
            // Cập nhật orderStatus thành true để đánh dấu đơn hàng đã được tạo
            FoodOrder order = cartOrder.get();
            order.setOrderStatus(true);

            // Lưu lại đơn hàng với status đã cập nhật
            foodOrderRepository.save(order);

            // Trả về đơn hàng đã được cập nhật
            return order;
        }

        // Nếu không tìm thấy giỏ hàng, trả về lỗi
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No cart found for customer.");
    }


    /**
     * Retrieves a list of orders placed by a specific customer.
     * @return ApiResponse containing a list of FoodOrder.
     */
    public ApiResponse<List<FoodOrder>> getOrderList(int customerId) {
        List<FoodOrder> orders = foodOrderRepository.findByCustomer_IdAndOrderStatus(customerId, true);
        return ApiResponse.build(1000, "Success", orders);
    }

    /**
     * Retrieves details of a specific order.
     *
     * @param orderId ID of the order.
     * @param customerId ID of the logged-in customer.
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
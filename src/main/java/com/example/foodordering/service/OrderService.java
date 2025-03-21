package com.example.foodordering.service;

import com.example.foodordering.dto.request.OrderRequest;
import com.example.foodordering.dto.request.UpdateOrderStatusRequest;
import com.example.foodordering.dto.response.ApiResponse;
import com.example.foodordering.entity.Customer;
import com.example.foodordering.entity.FoodOrder;
import com.example.foodordering.entity.OrderStatus;
import com.example.foodordering.repository.CustomerRepository;
import com.example.foodordering.repository.FoodOrderRepository;
import com.example.foodordering.repository.OrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private FoodOrderRepository foodOrderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    /**
     * Tạo đơn hàng mới
     */
    public ApiResponse<FoodOrder> createOrder(OrderRequest orderRequest) {
        ApiResponse<FoodOrder> response = new ApiResponse<>();

        Optional<Customer> customerOpt = customerRepository.findById(orderRequest.getCustomerId());
        if (customerOpt.isEmpty()) {
            response.setCode(1404);
            response.setMessage("Khách hàng không tồn tại!");
            response.setData(null);
            return response;
        }

        OrderStatus pendingStatus = orderStatusRepository.findByStatusValue("Pending").orElse(null);
        if (pendingStatus == null) {
            response.setCode(1400);
            response.setMessage("Không tìm thấy trạng thái đơn hàng!");
            response.setData(null);
            return response;
        }

        FoodOrder newOrder = new FoodOrder();
        newOrder.setCustomer(customerOpt.get());
        newOrder.setOrderStatus(pendingStatus);
        newOrder.setTotalAmount(orderRequest.getTotalAmount());

        foodOrderRepository.save(newOrder);

        response.setCode(1201);
        response.setMessage("Đơn hàng đã được tạo thành công!");
        response.setData(newOrder);
        return response;
    }

    /**
     * Cập nhật trạng thái đơn hàng
     */
    public ApiResponse<FoodOrder> updateOrderStatus(UpdateOrderStatusRequest request) {
        ApiResponse<FoodOrder> response = new ApiResponse<>();

        Optional<FoodOrder> orderOpt = foodOrderRepository.findById(request.getOrderId());
        if (orderOpt.isEmpty()) {
            response.setCode(1404);
            response.setMessage("Không tìm thấy đơn hàng!");
            response.setData(null);
            return response;
        }

        Optional<OrderStatus> statusOpt = orderStatusRepository.findByStatusValue(request.getNewStatus());
        if (statusOpt.isEmpty()) {
            response.setCode(1400);
            response.setMessage("Trạng thái không hợp lệ!");
            response.setData(null);
            return response;
        }

        FoodOrder order = orderOpt.get();
        order.setOrderStatus(statusOpt.get());
        foodOrderRepository.save(order);

        response.setCode(1000);
        response.setMessage("Cập nhật trạng thái đơn hàng thành công!");
        response.setData(order);
        return response;
    }

    /**
     * Lấy danh sách đơn hàng theo khách hàng
     */
    public ApiResponse<List<FoodOrder>> getOrdersByCustomer(Integer customerId) {
        List<FoodOrder> orders = foodOrderRepository.findByCustomer_CustomerId(customerId);

        ApiResponse<List<FoodOrder>> response = new ApiResponse<>();
        response.setCode(1000);
        response.setMessage("Danh sách đơn hàng của khách hàng");
        response.setData(orders);

        return response;
    }

    /**
     * Xem chi tiết đơn hàng
     */
    public ApiResponse<FoodOrder> getOrderDetails(Integer orderId) {
        ApiResponse<FoodOrder> response = new ApiResponse<>();

        Optional<FoodOrder> orderOpt = foodOrderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            response.setCode(1404);
            response.setMessage("Không tìm thấy đơn hàng!");
            response.setData(null);
            return response;
        }

        response.setCode(1000);
        response.setMessage("Chi tiết đơn hàng");
        response.setData(orderOpt.get());
        return response;
    }
}

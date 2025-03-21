package com.example.foodordering.service;

import com.example.foodordering.dto.request.OrderRequest;
import com.example.foodordering.dto.request.UpdateOrderStatusRequest;
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
    public String createOrder(OrderRequest orderRequest) {
        Optional<Customer> customerOpt = customerRepository.findById(orderRequest.getCustomerId());
        if (customerOpt.isEmpty()) {
            return "Khách hàng không tồn tại!";
        }

        OrderStatus pendingStatus = orderStatusRepository.findByStatusValue("Pending").orElse(null);
        if (pendingStatus == null) {
            return "Không tìm thấy trạng thái đơn hàng!";
        }

        FoodOrder newOrder = new FoodOrder();
        newOrder.setCustomer(customerOpt.get());
        newOrder.setOrderStatus(pendingStatus);
        newOrder.setTotalAmount(orderRequest.getTotalAmount());

        foodOrderRepository.save(newOrder);
        return "Đơn hàng đã được tạo thành công!";
    }

    /**
     * Cập nhật trạng thái đơn hàng
     */
    public String updateOrderStatus(UpdateOrderStatusRequest request) {
        Optional<FoodOrder> orderOpt = foodOrderRepository.findById(request.getOrderId());
        if (orderOpt.isEmpty()) {
            return "Không tìm thấy đơn hàng!";
        }

        Optional<OrderStatus> statusOpt = orderStatusRepository.findByStatusValue(request.getNewStatus());
        if (statusOpt.isEmpty()) {
            return "Trạng thái không hợp lệ!";
        }

        FoodOrder order = orderOpt.get();
        order.setOrderStatus(statusOpt.get());
        foodOrderRepository.save(order);
        return "Cập nhật trạng thái đơn hàng thành công!";
    }

    /**
     * Lấy danh sách đơn hàng theo khách hàng
     */
    public List<FoodOrder> getOrdersByCustomer(Integer customerId) {
        return foodOrderRepository.findByCustomerCustomerId(customerId);
    }

    /**
     * Xem chi tiết đơn hàng
     */
    public Optional<FoodOrder> getOrderDetails(Integer orderId) {
        return foodOrderRepository.findById(orderId);
    }
}

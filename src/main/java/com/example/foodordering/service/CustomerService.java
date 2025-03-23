package com.example.foodordering.service;

import com.example.foodordering.dto.request.OrderRequest;
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
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private FoodOrderRepository foodOrderRepository;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    /**
     * Cập nhật thông tin khách hàng
     */
    public ApiResponse<Customer> updateProfile(int customerId, Customer updatedCustomer) {
        ApiResponse<Customer> response = new ApiResponse<>();

        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isEmpty()) {
            response.setCode(1404);
            response.setMessage("Không tìm thấy khách hàng!");
            response.setData(null);
            return response;
        }

        Customer customer = customerOpt.get();
        customer.setFirstName(updatedCustomer.getFirstName());
        customer.setLastName(updatedCustomer.getLastName());
        customer.setPhoneNumber(updatedCustomer.getPhoneNumber());
        customerRepository.save(customer);

        response.setCode(1000);
        response.setMessage("Cập nhật thông tin thành công!");
        response.setData(customer);
        return response;
    }

    /**
     * Đặt hàng mới
     */
    public ApiResponse<FoodOrder> placeOrder(OrderRequest orderRequest) {
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
     * Theo dõi trạng thái đơn hàng
     */
    public ApiResponse<String> trackOrder(int orderId) {
        ApiResponse<String> response = new ApiResponse<>();

        Optional<FoodOrder> orderOpt = foodOrderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            response.setCode(1404);
            response.setMessage("Không tìm thấy đơn hàng!");
            response.setData(null);
            return response;
        }

        response.setCode(1000);
        response.setMessage("Trạng thái đơn hàng");
        response.setData(orderOpt.get().getOrderStatus().getStatusValue());
        return response;
    }

    /**
     * Xem chi tiết đơn hàng
     */
    public ApiResponse<FoodOrder> getOrderDetails(int orderId) {
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

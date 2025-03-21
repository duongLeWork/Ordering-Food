package com.example.foodordering.service;

import com.example.foodordering.dto.request.OrderRequest;
import com.example.foodordering.entity.Customer;
import com.example.foodordering.entity.FoodOrder;
import com.example.foodordering.entity.OrderStatus;
import com.example.foodordering.repository.CustomerRepository;
import com.example.foodordering.repository.FoodOrderRepository;
import com.example.foodordering.repository.OrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public String updateProfile(Integer customerId, Customer updatedCustomer) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            customer.setFirstName(updatedCustomer.getFirstName());
            customer.setLastName(updatedCustomer.getLastName());
            customer.setPhoneNumber(updatedCustomer.getPhoneNumber());
            customerRepository.save(customer);
            return "Cập nhật thông tin thành công!";
        }
        return "Không tìm thấy khách hàng!";
    }

    /**
     * Đặt hàng mới
     */
    public String placeOrder(OrderRequest orderRequest) {
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
     * Theo dõi trạng thái đơn hàng
     */
    public String trackOrder(Integer orderId) {
        Optional<FoodOrder> orderOpt = foodOrderRepository.findById(orderId);
        return orderOpt.map(foodOrder -> "Trạng thái đơn hàng: " + foodOrder.getOrderStatus().getStatusValue())
                .orElse("Không tìm thấy đơn hàng!");
    }

    /**
     * Xem chi tiết đơn hàng
     */
    public Optional<FoodOrder> getOrderDetails(Integer orderId) {
        return foodOrderRepository.findById(orderId);
    }
}

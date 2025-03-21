package com.example.foodordering.controller;

import com.example.foodordering.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/* LƯU Ý:
* Controller này được tạo ra để test nghịch API sử dụng curl và bash trên Linux.
* Chạy cái này là toàn bộ cơ sở dữ liệu sẽ bốc hơi =)))
* */

@RestController
@RequestMapping("/test")
public class TestAPIController {

    @Autowired private OrderMenuItemRepository orderMenuItemRepository;
    @Autowired private FoodOrderRepository foodOrderRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private AccountRepository accountRepository;
    @Autowired private FoodRepository foodRepository;
    @Autowired private OrderStatusRepository orderStatusRepository;
//    @Autowired private ManagerRepository managerRepository;
//    @Autowired private RestaurantRepository restaurantRepository;
//    @Autowired private AddressRepository addressRepository;
//    @Autowired private CustomerAddressRepository customerAddressRepository;

    @DeleteMapping("/cleanup")
    public String cleanupDatabase() {
        // Xóa theo thứ tự để tránh lỗi ràng buộc khóa ngoại (FK constraints)
        orderMenuItemRepository.deleteAll();
        foodOrderRepository.deleteAll();
        customerRepository.deleteAll();
        foodRepository.deleteAll();
        orderStatusRepository.deleteAll();
        accountRepository.deleteAll();
//        customerAddressRepository.deleteAll();
//        managerRepository.deleteAll();
//        restaurantRepository.deleteAll();
//        addressRepository.deleteAll();
        return "✅ Database cleaned!";
    }
}

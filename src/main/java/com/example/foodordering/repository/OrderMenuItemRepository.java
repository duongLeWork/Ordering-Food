package com.example.foodordering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.foodordering.entity.OrderMenuItem;

import java.util.List;

@Repository
public interface OrderMenuItemRepository extends JpaRepository<OrderMenuItem, Integer> {
    List<OrderMenuItem> findByFoodOrderId(Integer foodOrderId);
    /* Truy vấn OrderMenuItem dựa vào FoodOrder → Customer (Để không phải thay đổi cấu trúc của CSDL)
    * Spring Boot sẽ tự động chuyển đổi tên hàm thành SQL:
    * ===========================================================
    * SELECT * FROM order_menu_item omi
    * JOIN food_order fo ON omi.food_order_id = fo.food_order_id
    * WHERE fo.customer_id = ?;
    * ===========================================================
    * Tên Function khá quan trọng =)) Ý kiến cập nhật ping thằng @duy103zxc nhé.
    * */
    List<OrderMenuItem> findByFoodOrder_Customer_CustomerId(Integer customerId);
    void deleteByFoodOrder_Customer_CustomerId(Integer customerId);

}

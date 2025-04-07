package com.example.foodordering.repository;

import com.example.foodordering.entity.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, Integer> {
    /**
     * Tìm danh sách đơn hàng của một khách hàng dựa trên trạng thái đơn hàng.
     * SELECT * FROM food_order fo
     * WHERE fo.customer_id = ? AND fo.order_status = ?;
     * @param customerId  ID của khách hàng.
     * @param statusValue Giá trị trạng thái của đơn hàng (true: đã đặt hàng, false: trong giỏ hàng).
     * @return Danh sách đơn hàng thỏa mãn điều kiện.
     */
    List<FoodOrder> findByCustomer_IdAndOrderStatus(int customerId, boolean statusValue);
    List<FoodOrder> findByOrderStatus(boolean statusValue);
}
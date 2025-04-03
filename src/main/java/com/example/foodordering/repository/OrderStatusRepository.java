package com.example.foodordering.repository;

import com.example.foodordering.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository cho thực thể {@link OrderStatus}.
 * Cung cấp phương thức truy vấn dữ liệu liên quan đến trạng thái đơn hàng.
 */
@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
    /**
     * Tìm trạng thái đơn hàng dựa trên giá trị trạng thái.

     * SELECT * FROM order_status os
     * WHERE os.status_value = ?;

     * @param statusValue Giá trị trạng thái của đơn hàng (true: đã đặt hàng, false: trong giỏ hàng).
     * @return Trạng thái đơn hàng tương ứng nếu tìm thấy.
     */
    Optional<OrderStatus> findByStatusValue(boolean statusValue);
}

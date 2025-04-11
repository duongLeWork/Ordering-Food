package com.example.foodordering.repository;

import com.example.foodordering.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
    // You can add custom queries here if needed
}

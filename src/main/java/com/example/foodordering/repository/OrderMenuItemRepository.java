package com.example.foodordering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.foodordering.entity.OrderMenuItem;

import java.util.List;

@Repository
public interface OrderMenuItemRepository extends JpaRepository<OrderMenuItem, Integer> {
    List<OrderMenuItem> findByFoodOrderFoodOrderId(Integer foodOrderId);
}

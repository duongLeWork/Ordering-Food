package com.example.foodordering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.foodordering.entity.FoodOrder;

import java.util.List;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, Integer> {
    List<FoodOrder> findByCustomerCustomerId(Integer customerId);
    List<FoodOrder> findByOrderStatusOrderStatusId(Integer orderStatusId);
}

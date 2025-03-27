package com.example.foodordering.repository;

import com.example.foodordering.entity.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, Integer> {
    Optional<FoodOrder> findByCustomer_IdAndOrderStatus_StatusValue(int customerId, boolean statusValue);
}
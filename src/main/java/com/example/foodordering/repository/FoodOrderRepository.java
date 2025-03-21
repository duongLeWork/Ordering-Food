package com.example.foodordering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.foodordering.entity.FoodOrder;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, Integer> {
    List<FoodOrder> findByCustomerId(Integer customerId);
    List<FoodOrder> findByOrderStatusId(Integer orderStatusId);
    @Query("SELECT SUM(f.totalAmount) FROM FoodOrder f")
    BigDecimal calculateTotalSales();
}

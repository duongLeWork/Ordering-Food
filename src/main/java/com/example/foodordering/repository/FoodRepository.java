package com.example.foodordering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.foodordering.entity.Food;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {
    // Các phương thức truy vấn tùy chỉnh có thể được thêm vào đây
}

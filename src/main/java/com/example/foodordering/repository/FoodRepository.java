package com.example.foodordering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.foodordering.entity.Food;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {

    List<Food> findByNameContainingIgnoreCase(String name);
    List<Food> findByIsAvailableTrue();

    // Filter by category
    List<Food> findByCategoryAndIsAvailableTrue(String category);

    // Custom query for similar dishes
    @Query("SELECT f FROM Food f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%')) AND f.isAvailable = true")
    List<Food> findSimilarDishes(@Param("keyword") String keyword);
}

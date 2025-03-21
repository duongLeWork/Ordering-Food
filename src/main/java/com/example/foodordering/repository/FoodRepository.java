package com.example.foodordering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.foodordering.entity.Food;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {
    List<Food> findByNameContainingIgnoreCase(String name);
    List<Food> findByIsAvailableTrue();
}

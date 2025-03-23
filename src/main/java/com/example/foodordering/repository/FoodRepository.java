package com.example.foodordering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.foodordering.entity.Food;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {

    List<Food> findByNameContainingIgnoreCase(String name);
    @Query("SELECT f FROM Food f WHERE f.isAvailable = true ORDER BY f.price :order")
    List<Food> findAvailableDishesSorted(@Param("order") String order);
    List<Food> findByIsAvailableTrue();
    // Filter by category
    List<Food> findByCategoryAndIsAvailableTrue(String category);

    @Query("SELECT f FROM Food f WHERE f.isAvailable = true AND f.price BETWEEN :minPrice AND :maxPrice AND f.foodId <> :excludeFoodId")
    List<Food> findSimilarDishes(@Param("minPrice") BigDecimal minPrice,
                                 @Param("maxPrice") BigDecimal maxPrice,
                                 @Param("excludeFoodId") int excludeFoodId);
    /*
    * It retrieves the first matching food item based on the keyword (case-insensitive).
    * This food item serves as the reference to find similar dishes based on category and price range.
    * */
    Optional<Food> findFirstByNameContainingIgnoreCase(String name);

}

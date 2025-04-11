package com.example.foodordering.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String firstname;
    String lastname;
    String phoneNumber;
    String address;

    @OneToOne
    @JoinColumn(name = "account_id")
    Account account;

    @OneToMany(mappedBy = "customer")
    List<FoodOrder> foodOrders;

    // Transient fields for total items and total spent, not persisted in the database
    @Transient
    private int totalItems;

    @Transient
    private BigDecimal totalSpent;

    // Helper method to calculate totalItems and totalSpent
    public void calculateTotals() {
        this.totalItems = foodOrders.stream().mapToInt(FoodOrder::getTotalItems).sum();
        this.totalSpent = foodOrders.stream().map(FoodOrder::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

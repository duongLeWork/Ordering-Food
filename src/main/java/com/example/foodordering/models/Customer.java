package com.example.foodordering.models;

import com.example.foodordering.models.Order.FoodOrder;
import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "Customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Customer_ID")
    private Long customerId;

    @Column(name = "Name")
    private String name;

    @OneToMany(mappedBy = "customer")
    private List<FoodOrder> foodOrders;

    public Customer(Long customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }
    // Getters and Setters
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}



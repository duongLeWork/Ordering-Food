package com.example.foodordering.models;

import com.example.foodordering.models.Order.FoodOrder;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;


@Entity
public class Customer {
    private Long id;
    private String name;
    private String phoneNo;
    private String email;
    private BigDecimal totalSpent;
    private int lastActiveOrders;

    // Constructor
    public Customer(Long id, String name, String phoneNo, String email, BigDecimal totalSpent, int lastActiveOrders) {
        this.id = id;
        this.name = name;
        this.phoneNo = phoneNo;
        this.email = email;
        this.totalSpent = totalSpent;
        this.lastActiveOrders = lastActiveOrders;
    }
    public Customer(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }

    public int getLastActiveOrders() {
        return lastActiveOrders;
    }

    public void setLastActiveOrders(int lastActiveOrders) {
        this.lastActiveOrders = lastActiveOrders;
    }
}



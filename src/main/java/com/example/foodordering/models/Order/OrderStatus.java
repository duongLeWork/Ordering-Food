package com.example.foodordering.models.Order;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class OrderStatus {
    private Long orderStatusId;
    private String status;
    private List<FoodOrder> foodOrders;

    // Constructor
    public OrderStatus(Long orderStatusId, String status) {
        this.orderStatusId = orderStatusId;
        this.status = status;
    }

    // Getters and Setters
    public Long getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(Long orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


package com.example.foodordering.models.Order;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Order_Status")
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Order_status_ID")
    private Long orderStatusId;

    @Column(name = "Status")
    private String status;

    @OneToMany(mappedBy = "orderStatus")
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


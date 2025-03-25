package com.example.foodordering.models.Order;

import jakarta.persistence.*;

import java.util.List;


@Entity
public class OrderMenu {
    private Long orderMenuId;
    private String menuItem;
    private List<FoodOrder> foodOrders;

    // Constructor
    public OrderMenu(Long orderMenuId, String menuItem) {
        this.orderMenuId = orderMenuId;
        this.menuItem = menuItem;
    }

    // Getters and Setters
    public Long getOrderMenuId() {
        return orderMenuId;
    }

    public void setOrderMenuId(Long orderMenuId) {
        this.orderMenuId = orderMenuId;
    }

    public String getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(String menuItem) {
        this.menuItem = menuItem;
    }
}



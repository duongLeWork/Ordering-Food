package com.example.foodordering.models.Order;

import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "Order_Menu")
public class OrderMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Order_Menu_ID")
    private Long orderMenuId;

    @Column(name = "Menu_Item")
    private String menuItem;

    @OneToMany(mappedBy = "orderMenu")
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



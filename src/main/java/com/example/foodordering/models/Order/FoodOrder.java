package com.example.foodordering.models.Order;

import com.example.foodordering.models.Customer;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class FoodOrder {
    private Long foodOrderId;
    private Customer customer;
    private OrderStatus orderStatus;
    private OrderMenu orderMenu;
    private BigDecimal totalAmount;
    private BigDecimal price;

    public FoodOrder(Long foodOrderId, Customer customer, OrderStatus orderStatus, OrderMenu orderMenu, BigDecimal totalAmount, BigDecimal price) {
        this.foodOrderId = foodOrderId;
        this.customer = customer;
        this.orderStatus = orderStatus;
        this.orderMenu = orderMenu;
        this.totalAmount = totalAmount;
        this.price = price;
    }
    // Getters and Setters
    public Long getFoodOrderId() {
        return foodOrderId;
    }

    public void setFoodOrderId(Long foodOrderId) {
        this.foodOrderId = foodOrderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderMenu getOrderMenu() {
        return orderMenu;
    }

    public void setOrderMenu(OrderMenu orderMenu) {
        this.orderMenu = orderMenu;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}

package com.example.foodordering.entity;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
public class FoodOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer foodOrderId;
    private BigDecimal totalAmount;
    private BigDecimal price; // Cái này khác gì totalAmount

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "order_status_id")
    private OrderStatus orderStatus;

    @OneToMany
    @JoinColumn(name = "order_menu_id")
    private List<OrderMenuItem> orderMenuItems;
}
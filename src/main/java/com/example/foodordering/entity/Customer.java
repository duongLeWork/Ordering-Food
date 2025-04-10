package com.example.foodordering.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

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
    BigDecimal totalSpent;
    int orderCount;

    @OneToOne
    @JoinColumn(name = "account_id")
    Account account;
}

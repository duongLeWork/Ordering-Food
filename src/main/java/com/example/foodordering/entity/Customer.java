package com.example.foodordering.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customerId;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "account_id", unique = true, nullable = false)
    private Account account;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<CustomerAddress> customerAddresses;

}
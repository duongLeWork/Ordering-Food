package com.example.foodordering.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity

public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountId;
    private String username;
    private String password;
    private String email;
    private String role;

    @OneToOne(mappedBy = "account")
    private Manager manager;

    @OneToOne(mappedBy = "account")
    private Customer customer;
}

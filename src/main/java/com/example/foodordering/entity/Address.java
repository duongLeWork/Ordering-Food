package com.example.foodordering.entity;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addressId;
    private String streetNumber;
    private String city;
    private String country;

    @OneToOne(mappedBy = "address")
    private Restaurant restaurant;

    @OneToOne(mappedBy = "address")
    private CustomerAddress customerAddress;
}
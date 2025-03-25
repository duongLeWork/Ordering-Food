package com.example.foodordering.models;

public class Category {

    private int id;
    private String name;
    private String variantPrice;  // Price type (e.g., Additional Price, Specific Price)
    private boolean status;       // Status (active or inactive)

    // Constructor
    public Category(int id, String name, String variantPrice) {
        this.id = id;
        this.name = name;
        this.variantPrice = variantPrice;
        this.status = true; // Default status is active
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVariantPrice() {
        return variantPrice;
    }

    public void setVariantPrice(String variantPrice) {
        this.variantPrice = variantPrice;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

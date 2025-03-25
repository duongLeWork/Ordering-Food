package com.example.foodordering.models;

public class Category {

    private int id;
    private String name;
    private String variantPrice;  // Price type (e.g., Additional Price, Specific Price)
    private boolean isAvailable;       // Status (active or inactive)

    // Constructor
    public Category(int id, String name, String variantPrice) {
        this.id = id;
        this.name = name;
        this.variantPrice = variantPrice;
        this.isAvailable = true; // Default status is active
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

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}

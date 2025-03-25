package com.example.foodordering.models;

public class PaymentMethod {
    private String name;
    private String description;
    private boolean active;

    // Constructor
    public PaymentMethod(String name, String description, boolean active) {
        this.name = name;
        this.description = description;
        this.active = active;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

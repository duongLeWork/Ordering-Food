package com.example.foodordering.models;

public class Product {
    private int id;
    private String name;
    private double price;
    private int stock;
    private String imageUrl;
    private boolean status;

    public Product(int id, String name, double price, int stock, String imageUrl, boolean status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.status = status;
    }

    public Product(String s, String image, double price, int stock, boolean b) {
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getImageUrl() { return imageUrl; }
    public boolean isStatus() { return status; }
}

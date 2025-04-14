package com.example.foodordering.dto.response;

public class MonthlySalesData {
    private String month;
    private double saleAmount;
    private double revenue;

    // Constructor, getters, and setters
    public MonthlySalesData(String month, double saleAmount, double revenue) {
        this.month = month;
        this.saleAmount = saleAmount;
        this.revenue = revenue;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(double saleAmount) {
        this.saleAmount = saleAmount;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
}


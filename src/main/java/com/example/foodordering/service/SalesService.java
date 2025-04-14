package com.example.foodordering.service;

import com.example.foodordering.dto.response.MonthlySalesData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SalesService {

    // Example method to get monthly sales data
    public List<MonthlySalesData> getMonthlySalesData() {
        List<MonthlySalesData> salesData = new ArrayList<>();

        // Example data, replace with actual logic to fetch data from database
        salesData.add(new MonthlySalesData("Jan", 8.5, 6.3));
        salesData.add(new MonthlySalesData("Feb", 8.6, 6.5));
        salesData.add(new MonthlySalesData("Mar", 8.4, 6.2));
        salesData.add(new MonthlySalesData("Apr", 8.2, 6.1));
        salesData.add(new MonthlySalesData("May", 8.8, 6.7));
        salesData.add(new MonthlySalesData("Jun", 8.7, 6.6));

        return salesData;
    }
}

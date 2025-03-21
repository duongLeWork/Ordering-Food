package com.example.foodordering.controller;

import com.example.foodordering.dto.request.AccountCreationRequest;
import com.example.foodordering.entity.Food;
import com.example.foodordering.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guest")
public class GuestController {

    @Autowired
    private GuestService guestService;

    @GetMapping("/dishes")
    public List<Food> getAvailableDishes() {
        return guestService.getAvailableDishes();
    }

    @GetMapping("/search")
    public List<Food> searchDishes(@RequestParam String keyword) {
        return guestService.searchDishes(keyword);
    }

    @PostMapping("/register")
    public String register(@RequestBody AccountCreationRequest accountRequest) {
        return guestService.createAccount(accountRequest);
    }
}
